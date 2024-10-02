package com.dominikcebula.spring.security.user.authentication.passwordreset.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetEmailData {
    @NotBlank(message = "Email address cannot be empty.")
    @Email(message = "Must be a valid e-mail address.")
    private String email;
}
