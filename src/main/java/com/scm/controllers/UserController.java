package com.scm.controllers;
import com.scm.dto.PasswordDto;
import com.scm.dto.UserDto;
import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.scm.services.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    private User user;
    private final UserService userService;

    private final ModelMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(ModelMapper mapper, UserService userService) {
        this.mapper = mapper;
        this.userService = userService;
    }

    @ModelAttribute
    public void addLoggedInUser(Authentication authentication,Model model){
        String emailOfLoggedInUser = Helper.getEmailOfLoggedInUser(authentication);
        user = userService.getUserByEmail(emailOfLoggedInUser);
        model.addAttribute("user", user);
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        return "user/profile";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        model.addAttribute("userDto",mapper.map(user, UserDto.class));
        return "user/edit_profile";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
//        if(bindingResult.hasErrors()){
//            return "user/edit_profile";
//        }
        User user = userService.updateUser(mapper.map(userDto, User.class), userDto.getUserImage());
        model.addAttribute("userDto",mapper.map(user, UserDto.class));
        redirectAttributes.addFlashAttribute("message",new Message("User updated successfully","success"));
        return "redirect:/user/edit";
    }

    @GetMapping("/edit-password")
    public String editPassword(Model model) {
        model.addAttribute("passwordDto",new PasswordDto());
        return "user/change_password";
    }


    @PostMapping("/password-change")
    public String changePassword(
            @ModelAttribute PasswordDto passwordDto,
            Model model,RedirectAttributes redirectAttributes){
        if(passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())){
            boolean isSuccess = userService.changePassword(passwordDto, user);
            if(isSuccess){
                logger.info("Password changed successfully");
                redirectAttributes.addFlashAttribute("message",new Message("Password changed successfully","success"));
            }else {
                logger.info("Password change failed");
                redirectAttributes.addFlashAttribute("message",new Message("Old password is incorrect","error"));
            }
        }
        else{
            redirectAttributes.addFlashAttribute("message",new Message("Confirm password doesn't matches","error"));
        }
        return "redirect:/user/edit-password";

    }

}

