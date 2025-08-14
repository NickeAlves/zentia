package com.api.zentia.dto.user.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateUserDTO(@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
                            String name,
                            String lastName,
                            @Email(message = "Email must be valid")
                            String email) {
}
