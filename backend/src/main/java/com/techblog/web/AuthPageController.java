package com.techblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping({"/register", "/signup"})
    public String registerPage() {
        return "auth/register";
    }

    @GetMapping("/profile")
    public String profilePage() {
        return "user/profile";
    }

    @GetMapping("/admin/users")
    public String adminUsersPage() {
        return "admin/users";
    }
}
