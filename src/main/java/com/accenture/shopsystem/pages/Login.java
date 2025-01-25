package com.accenture.shopsystem.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Login {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
