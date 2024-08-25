package com.ac.su.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    // 유저 조회
    @GetMapping("/users/{userId}")
    public String getUserById(@PathVariable("userId") Long userId, Model model) {
        Optional<User> user = userService.getUserById(userId);
        model.addAttribute("user", user.orElse(null));
        return "user";
    }
}
