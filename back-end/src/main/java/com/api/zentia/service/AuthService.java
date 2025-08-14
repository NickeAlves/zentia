package com.api.zentia.service;

import com.api.zentia.dto.user.request.LoginUserDTO;
import com.api.zentia.dto.user.request.RegisterUserDTO;
import com.api.zentia.dto.user.response.DataUserDTO;
import com.api.zentia.dto.user.response.ResponseUserDTO;
import com.api.zentia.entity.User;
import com.api.zentia.repository.UserRepository;
import com.api.zentia.security.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    );

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<ResponseUserDTO> register(RegisterUserDTO dto) {
        try {
            validateRegistrationData(dto);

            String email = dto.email().trim().toLowerCase();

            if (userRepository.existsByEmail(email)) {
                logger.warn("Registration attempt with existing email: {}", email);
                return ResponseEntity.badRequest().body(new ResponseUserDTO(false, null, "Email already in use."));
            }

            User user = new User();
            user.generateId();
            user.setName(capitalizeFirstLetters(dto.name()));
            user.setLastName(capitalizeFirstLetters(dto.lastName()));
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(dto.password()));

            User savedUser = userRepository.save(user);
            String token = tokenService.generateToken(savedUser);

            logger.info("Customer registered successfully: {}", email);

            return ResponseEntity.ok(new ResponseUserDTO(true, token, "Registered successfully", createCustomerData(savedUser)));
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.warn("Registration validation failed: {}", illegalArgumentException.getMessage());
            return ResponseEntity.badRequest().body(new ResponseUserDTO(false, null, illegalArgumentException.getMessage(), null));
        } catch (Exception exception) {
            logger.error("Unexpected error during registration: ", exception);
            return ResponseEntity.internalServerError().body(new ResponseUserDTO(false, null, "An unexpected error occurred", null));
        }

    }

    public ResponseEntity<ResponseUserDTO> login(LoginUserDTO dto) {
        try {
            String email = dto.email().trim().toLowerCase();
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isEmpty()) {
                logger.warn("Email or password is incorrect");
                return ResponseEntity.status(404)
                        .body(new ResponseUserDTO(false, null, "Email or password is incorrect", null));
            }

            User user = optionalUser.get();

            if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
                logger.warn("Email or password is incorrect");
                return ResponseEntity.status(401)
                        .body(new ResponseUserDTO(false, null, "Email or password is incorrect", null));
            }

            String token = tokenService.generateToken(user);

            logger.info("User logged in successfully");
            return ResponseEntity.ok()
                    .body(new ResponseUserDTO(true, token, "Logged in successfully", null));
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.warn("Login validation failed: {}", illegalArgumentException.getMessage());
            return ResponseEntity.badRequest().body(new ResponseUserDTO(false, null, illegalArgumentException.getMessage(), null));
        } catch (Exception exception) {
            logger.error("Unexpected error during login: ", exception);
            return ResponseEntity.internalServerError().body(new ResponseUserDTO(false, null, "An unexpected error occurred", null));
        }
    }

    public ResponseEntity<ResponseUserDTO> logout() {
        try {
            logger.info("User logged out successfully");
            return ResponseEntity.ok(new ResponseUserDTO(true, null, "Logged out successfully", null));
        } catch (Exception exception) {
            logger.error("Error during logout");
            return ResponseEntity.internalServerError().body(new ResponseUserDTO(false, null, "An error occurred during logout", null));
        }
    }

    public ResponseEntity<ResponseUserDTO> refreshToken(String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401)
                        .body(new ResponseUserDTO(false, null, "Invalid authorization header"));
            }

            String token = authHeader.substring(7);
            String email = tokenService.validateToken(token);

            if (email == null) {
                return ResponseEntity.status(401)
                        .body(new ResponseUserDTO(false, null, "Invalid or expired token"));
            }

            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(new ResponseUserDTO(false, null, "User not found"));
            }

            User user = optionalUser.get();
            String newToken = tokenService.generateToken(user);

            logger.info("Token refreshed successfully for: {}", email);

            return ResponseEntity.ok(new ResponseUserDTO(true, newToken, "Token refreshed successfully"));
        } catch (Exception exception) {
            logger.error("Error refreshing token", exception);
            return ResponseEntity.internalServerError()
                    .body(new ResponseUserDTO(false, null, "An error occurred while refreshing token"));
        }
    }

    private void validateRegistrationData(RegisterUserDTO dto) {
        if (dto.name() == null || dto.name().isBlank()) {
            throw new IllegalArgumentException("The 'name' field is required");
        }
        if (dto.lastName() == null || dto.lastName().isBlank()) {
            throw new IllegalArgumentException("The 'lastName' field is required");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new IllegalArgumentException("The 'email' field is required");
        }
        if (!EMAIL_PATTERN.matcher(dto.email().trim()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (dto.password() == null || dto.password().length() < 6) {
            throw new IllegalArgumentException("Password must be a minimum of 6 characters");
        }
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

    private DataUserDTO createCustomerData(User user) {
        return new DataUserDTO(user.getId(), user.getName(), user.getLastName(), user.getEmail());
    }

}
