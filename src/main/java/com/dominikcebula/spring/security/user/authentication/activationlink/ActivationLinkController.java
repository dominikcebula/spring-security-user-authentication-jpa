package com.dominikcebula.spring.security.user.authentication.activationlink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.dominikcebula.spring.security.user.authentication.activationlink.ActivationLinkService.ActivationResult;

@Controller
public class ActivationLinkController {
    static final String ENDPOINT_ACTIVATE = "/activate";

    @Autowired
    private ActivationLinkService activationLinkService;

    @GetMapping(ENDPOINT_ACTIVATE)
    public String activate(@RequestParam(value = "token", required = false) String token, Model model) {
        ActivationResult activationResult = activationLinkService.activateAccount(token);

        model.addAttribute("activationResult", activationResult);

        return "activate";
    }

    @PostMapping("/regenerateActivationToken")
    public String regenerateActivationToken(@RequestParam(value = "token", required = false) String token) {
        // TODO

        return "activateTokenRegeneration";
    }
}
