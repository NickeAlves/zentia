package com.api.zentia.dto.user.response;

import java.util.UUID;

public record DataUserDTO(UUID id,
                          String name,
                          String lastName,
                          String email) {
}
