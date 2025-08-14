package com.api.zentia.service;

import com.api.zentia.dto.user.request.UpdateUserDTO;
import com.api.zentia.dto.user.response.DataDeleteUserDTO;
import com.api.zentia.dto.user.response.DataUserDTO;
import com.api.zentia.dto.user.response.DataUserUpdateDTO;
import com.api.zentia.dto.user.response.ResponseUserDTO;
import com.api.zentia.entity.User;
import com.api.zentia.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<List<DataUserDTO>> listAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<DataUserDTO> userDTOs = users.stream()
                .map(user -> new DataUserDTO(
                        user.getId(),
                        user.getName(),
                        user.getLastName(),
                        user.getEmail()
                ))
                .toList();

        return ResponseEntity.ok().body(userDTOs);
    }

    public ResponseEntity<ResponseUserDTO> findUserByEmail(String email) {
        String adjustedEmail = email.trim().toLowerCase();
        Optional<User> optionalUser = userRepository.findByEmail(adjustedEmail);

        if (optionalUser.isEmpty()) {
            logger.warn("User not found");
            return ResponseEntity.status(404)
                    .body(new ResponseUserDTO(false, null, "User not found", null));
        }

        User user = optionalUser.get();

        DataUserDTO dto = new DataUserDTO(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail()
        );

        return ResponseEntity.ok().body(new ResponseUserDTO(true, null, "User found", dto));
    }

    public ResponseEntity<ResponseUserDTO> findUserById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            logger.warn("User not found");
            return ResponseEntity.status(404)
                    .body(new ResponseUserDTO(false, null, "User not found", null));
        }

        User user = optionalUser.get();

        DataUserDTO dto = new DataUserDTO(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail()
        );

        return ResponseEntity.ok().body(new ResponseUserDTO(true, null, "User found", dto));
    }

    public ResponseEntity<DataUserUpdateDTO> updateUser(UUID id, UpdateUserDTO dto) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            logger.warn("User not found with id: {}", id);
            return ResponseEntity.status(404).body(new DataUserUpdateDTO(false, "User not found", null, null, null, null));
        }

        User existingUser = userOptional.get();

        if (dto.name() != null) existingUser.setName(capitalizeFirstLetters(dto.name()));
        if (dto.lastName() != null) existingUser.setLastName(capitalizeFirstLetters(dto.lastName()));


        if (dto.email() != null && !dto.email().isEmpty()) {
            String newEmail = dto.email().trim().toLowerCase();

            if (newEmail.equals(existingUser.getEmail())) {
                logger.warn("Email same as previous one. Please choose a different email.");
                return ResponseEntity.status(409)
                        .body(new DataUserUpdateDTO(false, "Email same as previous one. Please choose a different Email.", null, null, null, null));
            }

            existingUser.setEmail(newEmail);
        }

        userRepository.save(existingUser);
        logger.info("User updated successfully");
        return ResponseEntity.ok(new DataUserUpdateDTO(true, "User updated successfully.", existingUser.getId(), existingUser.getName(), existingUser.getLastName(), existingUser.getEmail()));

    }


    public ResponseEntity<DataDeleteUserDTO> deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) {
            logger.warn("User not found with id: {}", id);
            return ResponseEntity.status(404).body(new DataDeleteUserDTO(false,  "User not found"));
        }

        userRepository.deleteById(id);
        logger.info("User deleted successfully: {}", id);
        return ResponseEntity.ok(new DataDeleteUserDTO(true,  "User deleted successfully"));
    }

    public static String capitalizeFirstLetters(String input) {
        if (input == null || input.isEmpty()) return input;

        String[] words = input.trim().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    result.append(word.substring(1).toLowerCase());
                }
                result.append(" ");
            }
        }
        return result.toString().trim();
    }
}
