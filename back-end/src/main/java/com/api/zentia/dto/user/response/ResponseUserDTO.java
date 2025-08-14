package com.api.zentia.dto.user.response;

public record ResponseUserDTO(boolean success,
                              String token,
                              String message,
                              DataUserDTO user) {
    public ResponseUserDTO(boolean success, String token, String message) {
        this(success, token, message, null);
    }
}
