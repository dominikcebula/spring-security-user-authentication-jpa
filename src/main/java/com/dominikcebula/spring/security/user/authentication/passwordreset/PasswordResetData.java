package com.dominikcebula.spring.security.user.authentication.passwordreset;

import com.dominikcebula.spring.security.user.authentication.common.validation.password.PasswordDataProvider;
import com.dominikcebula.spring.security.user.authentication.common.validation.password.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@PasswordMatch(message = "Password and repeated password must match.")
public class PasswordResetData implements PasswordDataProvider {
    @NotBlank(message = "Token cannot be empty.")
    private String token;
    @NotBlank(message = "Password cannot be empty.")
    private String password;
    @NotBlank(message = "Repeated password cannot be empty.")
    private String repeatedPassword;
}
