package com.dominikcebula.spring.security.user.authentication.signup.web;

import com.dominikcebula.spring.security.user.authentication.signup.dto.SignupData;
import com.dominikcebula.spring.security.user.authentication.signup.service.SignupService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.dominikcebula.spring.security.user.authentication.signup.service.SignupService.UserRegistrationResult;
import static com.dominikcebula.spring.security.user.authentication.signup.service.SignupService.UserRegistrationResult.USER_ALREADY_EXISTS;
import static com.dominikcebula.spring.security.user.authentication.signup.service.SignupService.UserRegistrationResult.USER_DATA_INVALID;
import static com.dominikcebula.spring.security.user.authentication.spring.validation.BindingResultMapper.execute;

@Controller
public class SignupController {
    @Autowired
    private SignupService signupService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupData", new SignupData());

        return "signup";
    }

    @PostMapping("/signup")
    public String processSignupRequest(@ModelAttribute SignupData signupData, BindingResult bindingResult, HttpServletRequest request) {
        UserRegistrationResult result = execute(() -> signupService.registerUser(signupData), bindingResult, USER_DATA_INVALID);

        if (result == USER_ALREADY_EXISTS)
            bindingResult.rejectValue("email", "email.already.exists", "Sorry, it looks like this username is already taken.");

        if (bindingResult.hasErrors())
            return "signup";

        return "redirect:/signupSuccessful";
    }

    @GetMapping("/signupSuccessful")
    public String signupSuccessful() {
        return "signupSuccessful";
    }
}
