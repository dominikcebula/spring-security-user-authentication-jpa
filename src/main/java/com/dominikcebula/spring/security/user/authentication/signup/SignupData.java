package com.dominikcebula.spring.security.user.authentication.signup;

import lombok.Data;

@Data
public class SignupData {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String repeatedPassword;
}
