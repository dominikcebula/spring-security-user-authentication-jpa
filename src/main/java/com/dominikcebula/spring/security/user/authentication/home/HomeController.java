package com.dominikcebula.spring.security.user.authentication.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }
}
