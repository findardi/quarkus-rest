package org.acme.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegister(
    @NotBlank(message = "Fullname is required")
    @Size(min = 8, max = 255, message = "Fullname must be between 8-255 characters")
    String fullname,
    
    @NotBlank(message = "Username is required")
    @Size(min = 8, max = 255, message = "Username must be between 8-25 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    String username,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password,

    String role
) {
    public String getRoleOrDefault() {
        return (role == null || role.isBlank()) ? "USER" : role;
    }
}
