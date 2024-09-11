package com.scm.controllers;

import com.scm.dto.UserDto;
import com.scm.entities.User;
import com.scm.exceptions.ResourceNotFoundException;
import com.scm.exceptions.UnauthorizedException;
import com.scm.helper.Message;

import com.scm.services.UserService;
import jakarta.validation.Valid;
import org.apache.http.HttpException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {
	private static final Logger log = LoggerFactory.getLogger(PageController.class);
	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper mapper;

	@GetMapping("/")
	public String index() {
		return "redirect:/home";
	}

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("username", "Vishal Narsinh");
		model.addAttribute("youtube", "Learn Code with Vishal");
		model.addAttribute("github", "http://github.com/VishalNarsinh");
		return "home";
	}

	@GetMapping("/about")
	public String about() {
		System.out.println("About page called");
		return "about";
	}

	@GetMapping("/services")
	public String services() {
		System.out.println("services page called");
		return "services";
	}

	@GetMapping("/login")
	public String login() {
		System.out.println("login page called");
		return "login";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("userDto", new UserDto());
		return "signup";
	}

	@GetMapping("/contact")
	public String contact() {
		System.out.println("contact page called");
		return "contact";
	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.info("{}", bindingResult);
			return "signup";
		}
		System.out.println(userDto);
		User user = mapper.map(userDto, User.class);
		User saveUser = userService.saveUser(user);
		if (saveUser != null) {
			redirectAttributes.addFlashAttribute("message", new Message("User registered successfully. A verification link is sent to your registered email address", "success"));
		} else {
			redirectAttributes.addFlashAttribute("message", new Message("Something went wrong", "error"));
		}
		return "redirect:/signup";
	}


}
