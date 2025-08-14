package com.api.zentia.controller;

import com.api.zentia.dto.user.request.UpdateUserDTO;
import com.api.zentia.dto.user.response.DataDeleteUserDTO;
import com.api.zentia.dto.user.response.DataUserDTO;
import com.api.zentia.dto.user.response.DataUserUpdateDTO;
import com.api.zentia.dto.user.response.ResponseUserDTO;
import com.api.zentia.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<DataUserDTO>> getAllUsers() {
        return userService.listAllUsers();
    }

    @GetMapping("/email")
    public ResponseEntity<ResponseUserDTO> getUserByEmail(@RequestParam String email) {
        return userService.findUserByEmail(email);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> getUserByEmail(@PathVariable UUID id) {
        return userService.findUserById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataUserUpdateDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserDTO dto) {
        return userService.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataDeleteUserDTO> deleteUserById(@PathVariable UUID id) {
        return userService.deleteUserById(id);
    }
}
