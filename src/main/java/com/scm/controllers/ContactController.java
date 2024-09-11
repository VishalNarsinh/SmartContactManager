package com.scm.controllers;

import com.scm.services.impl.ContactServiceImpl;
import com.scm.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.scm.dto.ContactDto;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import java.util.Map;


@Controller
@RequestMapping("/user/contact")
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);
    private User user = null;

    private final ModelMapper modelMapper;
    private final ContactServiceImpl contactServiceImpl;
    private final UserServiceImpl userServiceImpl;

    public ContactController(ModelMapper modelMapper, ContactServiceImpl contactServiceImpl,
            UserServiceImpl userServiceImpl) {
        this.modelMapper = modelMapper;
        this.contactServiceImpl = contactServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @ModelAttribute
    public void getLoggedInUser(Authentication authentication) {
        String emailOfLoggedInUser = Helper.getEmailOfLoggedInUser(authentication);
        user = userServiceImpl.getUserByEmail(emailOfLoggedInUser);
    }

    // add contact page
    @RequestMapping("/add")
    public String showContactPage(Model model) {
        model.addAttribute("contactDto", new ContactDto());
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute ContactDto contactDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("{}",bindingResult);
            return "user/add_contact";
        }
        Contact contact = modelMapper.map(contactDto, Contact.class);
        Contact contact1 = contactServiceImpl.saveContact(contact, user, contactDto.getContactImage());
        if (contact1 != null) {
            redirectAttributes.addFlashAttribute("message", new Message("Contact saved successfully", "success"));
        } else {
            redirectAttributes.addFlashAttribute("message", new Message("Something went wrong", "error"));
        }
        return "redirect:/user/contact/add";
    }

    @GetMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable Integer contactId,RedirectAttributes redirectAttributes){
        Contact contact = contactServiceImpl.getContactByContactId(contactId);
        if(user == contact.getUser()){
            contactServiceImpl.deleteContact(contactId);
            redirectAttributes.addFlashAttribute("message", new Message("Contact deleted successfully", "success"));
        }
        else{
            redirectAttributes.addFlashAttribute("message", new Message("You're not authorized for deleting this contact", "error"));
        }
        return  "redirect:/user/contact/show";
    }

    @GetMapping("/view/{contactId}")
    public String showUpdateContactPage(Model model, @PathVariable Integer contactId,RedirectAttributes redirectAttributes) {
        Contact contact = contactServiceImpl.getContactByContactId(contactId);
        if(user!=contact.getUser()){
            redirectAttributes.addFlashAttribute("message", new Message("You're unauthorized for editing this contact", "error"));
            return  "redirect:/user/contact/show";
        }
        model.addAttribute("contactDto", modelMapper.map(contact, ContactDto.class));
        return "user/update_contact";
    }

    @PostMapping("/update")
    public String updateContact(@Valid @ModelAttribute ContactDto contactDto,BindingResult bindingResult,Model model,RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            return "user/update_contact";
        }
        Contact contact = contactServiceImpl.updateContact(modelMapper.map(contactDto, Contact.class), user, contactDto.getContactImage());
        model.addAttribute("contactDto", modelMapper.map(contact, ContactDto.class));
        redirectAttributes.addFlashAttribute("message",new Message("Contact updated successfully","success"));
        return "redirect:/user/contact/view/"+contact.getContactId();
    }


    @GetMapping("/show")
    public String showContact(
            Model model,
            @RequestParam(name = "pageNumber",defaultValue = "0",required = false)int pageNumber,
            @RequestParam(name = "pageSize",defaultValue = "3",required = false)int pageSize,
            @RequestParam Map<String,String> requestParams) {
        String sortBy = requestParams.getOrDefault("sortBy", requestParams.getOrDefault("sortby", "name"));
        String sortOrder = requestParams.getOrDefault("sortOrder", requestParams.getOrDefault("sortorder", "ASC"));
        Page<Contact> pageContact = contactServiceImpl.getContactsByUser(user, pageNumber, pageSize, sortBy, sortOrder);

        model.addAttribute("pageContact",pageContact);
        requestParams.forEach((k,v)->{
            System.out.println(k+ ":" +v);
        });
        return "user/show_contact";
    }

    @GetMapping("/search")
    public String searchContact(

            @RequestParam("keyword") String keyword,
            @RequestParam(name = "field",defaultValue = "name",required = false) String field,
            @RequestParam(name = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(name = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(name = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = "ASC",required = false) String sortOrder,
            Model model){
        Page<Contact> contactPage = contactServiceImpl.search(user, field, keyword, pageNumber, pageSize, sortBy, sortOrder);
        model.addAttribute("pageContact",contactPage);
        model.addAttribute("field",field);
        model.addAttribute("keyword",keyword);
        return "user/search";
    }

}
