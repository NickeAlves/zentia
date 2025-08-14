package com.api.zentia.controller;

import com.api.zentia.dto.user.request.LoginUserDTO;
import com.api.zentia.dto.user.request.RegisterUserDTO;
import com.api.zentia.dto.user.response.ResponseUserDTO;
import com.api.zentia.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseUserDTO> register(@Valid @RequestBody RegisterUserDTO dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseUserDTO> login(@Valid @RequestBody LoginUserDTO dto) {
        return authService.login(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseUserDTO> logout() {
        return authService.logout();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        return authService.refreshToken(authHeader);
    }
}
