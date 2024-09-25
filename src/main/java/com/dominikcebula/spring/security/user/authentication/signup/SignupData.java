package com.dominikcebula.spring.security.user.authentication.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupData {
    @NotBlank(message = "Email address cannot be empty.")
    @Email(message = "Must be a valid e-mail address.")
    private String email;
    @NotBlank(message = "First Name cannot be empty.")
    private String firstName;
    @NotBlank(message = "Last Name cannot be empty.")
    private String lastName;
    @NotBlank(message = "Password cannot be empty.")
    private String password;
    @NotBlank(message = "Repeated password cannot be empty.")
    private String repeatedPassword;
}
