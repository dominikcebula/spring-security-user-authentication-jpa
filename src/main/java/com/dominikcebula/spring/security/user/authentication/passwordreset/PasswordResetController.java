package com.dominikcebula.spring.security.user.authentication.passwordreset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.dominikcebula.spring.security.user.authentication.passwordreset.PasswordResetService.PasswordResetResult;
import static com.dominikcebula.spring.security.user.authentication.passwordreset.PasswordResetService.PasswordResetResult.PASSWORD_RESET_STATUS_UNKNOWN;
import static com.dominikcebula.spring.security.user.authentication.passwordreset.PasswordResetService.SendPasswordResetLinkResult;
import static com.dominikcebula.spring.security.user.authentication.spring.validation.BindingResultMapper.execute;

@Controller
public class PasswordResetController {
    static final String ENDPOINT_PASSWORD_RESET_USING_TOKEN = "/passwordResetUsingToken";

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/passwordReset")
    public String passwordReset(@ModelAttribute PasswordResetEmailData passwordResetEmailData) {
        return "passwordReset";
    }

    @PostMapping("/sendPasswordResetLink")
    public String sendPasswordResetLink(@ModelAttribute PasswordResetEmailData passwordResetEmailData, BindingResult bindingResult, Model model) {
        SendPasswordResetLinkResult sendPasswordResetLinkResult = execute(() -> passwordResetService.createAndSendPasswordResetLink(passwordResetEmailData), bindingResult, SendPasswordResetLinkResult.PASSWORD_RESET_DATA_INVALID);

        model.addAttribute("sendPasswordResetLinkResult", sendPasswordResetLinkResult);

        if (bindingResult.hasErrors() || !sendPasswordResetLinkResult.isSuccessful())
            return "passwordReset";

        return "passwordResetLinkSent";
    }

    @GetMapping(ENDPOINT_PASSWORD_RESET_USING_TOKEN)
    public String passwordResetUsingToken(@RequestParam(value = "token", required = false) String token, @ModelAttribute PasswordResetData passwordResetData, Model model) {
        passwordResetData.setToken(token);
        model.addAttribute("passwordResetResult", PASSWORD_RESET_STATUS_UNKNOWN);

        return "passwordResetUsingToken";
    }

    @PostMapping(ENDPOINT_PASSWORD_RESET_USING_TOKEN)
    public String passwordResetUsingToken(@ModelAttribute PasswordResetData passwordResetData, BindingResult bindingResult, Model model) {
        PasswordResetResult passwordResetResult = execute(() -> passwordResetService.resetPasswordUsingToken(passwordResetData), bindingResult, PasswordResetResult.PASSWORD_RESET_DATA_INVALID);

        model.addAttribute("passwordResetResult", passwordResetResult);

        if (bindingResult.hasErrors() || !passwordResetResult.isSuccessful())
            return "passwordResetUsingToken";

        return "passwordResetUsingTokenSuccessful";
    }
}
