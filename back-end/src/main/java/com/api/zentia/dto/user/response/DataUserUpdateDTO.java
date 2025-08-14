package com.api.zentia.dto.user.response;

import java.util.UUID;

public record DataUserUpdateDTO(boolean success,
                                String message,
                                UUID id,
                                String name,
                                String lastName,
                                String email) {

    public DataUserUpdateDTO(boolean success, String message) {
        this(success, message, null, null, null, null);
    }
}
