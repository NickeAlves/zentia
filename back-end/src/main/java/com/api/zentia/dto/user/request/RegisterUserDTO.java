package com.api.zentia.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDTO(@NotBlank
                              @Size(max = 50)
                              String name,

                              @NotBlank
                              @Size(max = 50)
                              String lastName,

                              @Email
                              @NotBlank
                              String email,

                              @NotBlank(message = "Password cannot be blank")
                              @Size(min = 6, max = 100, message = "Password must be a minimum of 6 characters")
                              String password) {
}
