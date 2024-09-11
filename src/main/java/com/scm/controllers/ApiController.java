package com.scm.controllers;

import com.scm.dto.ContactDto;
import com.scm.entities.Contact;
import com.scm.helper.Message;
import com.scm.services.ContactService;
import com.scm.services.UserService;
import com.scm.services.impl.ContactServiceImpl;
import com.scm.services.impl.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api")
public class ApiController {


    private static final Logger log = LoggerFactory.getLogger(ApiController.class);
    private final ContactService contactServiceImpl;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public ApiController(ContactService contactServiceImpl, ModelMapper modelMapper, UserService userService) {
        this.contactServiceImpl = contactServiceImpl;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/contact/{contactId}")
    public ContactDto getContact(@PathVariable int contactId) {
        return modelMapper.map(contactServiceImpl.getContactByContactId(contactId), ContactDto.class);
    }

    @GetMapping("/auth/verify-email")
    public RedirectView verifyEmail(@RequestParam("token") String emailToken, RedirectAttributes redirectAttributes) {
        log.info("Email Token {}", emailToken);
        userService.verifyEmailFromToken(emailToken);
        redirectAttributes.addFlashAttribute("message", new Message("User verified successfully,now you can use our platform for managing your contacts.", "success"));
        return new RedirectView("/login");
    }

}
