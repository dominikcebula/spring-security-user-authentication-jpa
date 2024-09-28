package com.dominikcebula.spring.security.user.authentication.activationlink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ActivationLinkController {
    static final String ENDPOINT_ACTIVATE = "/activate";

    @Autowired
    private ActivationLinkService activationLinkService;

    @GetMapping(ENDPOINT_ACTIVATE)
    public String activate(@RequestParam("token") String token) {
        return "activate";
    }
}
