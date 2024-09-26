package com.dominikcebula.spring.security.user.authentication.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.dominikcebula.spring.security.user.authentication.spring.BindingResultMapper.execute;

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
    public String processSignupRequest(@ModelAttribute SignupData signupData, BindingResult bindingResult) {
        execute(() -> signupService.registerUser(signupData), bindingResult);

        if (bindingResult.hasErrors())
            return "signup";

        return "redirect:/signupSuccessful";
    }

    @GetMapping("/signupSuccessful")
    public String signupSuccessful() {
        return "signupSuccessful";
    }
}
