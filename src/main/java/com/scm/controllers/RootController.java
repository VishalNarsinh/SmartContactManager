package com.scm.controllers;

import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {
    private final UserService userService;

    public RootController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addLoggedInUser(Authentication authentication, Model model){
        if(authentication==null)
            return;
        String emailOfLoggedInUser = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(emailOfLoggedInUser);
        model.addAttribute("user", user);
    }
}
