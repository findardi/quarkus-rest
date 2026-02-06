package org.acme.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLogin(
    @NotBlank(message = "username is required")
    String username,

    @NotBlank(message = "password is required")
    String password
) {}
