package com.dominikcebula.spring.security.user.authentication.activationlink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ActivationLinkController {
    @Autowired
    private ActivationLinkService activationLinkService;

    @GetMapping("/activate")
    public String activate(@RequestParam("token") String token) {
        return "activate";
    }
}
