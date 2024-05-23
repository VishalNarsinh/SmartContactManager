package com.smart.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/forgot-password")
	public String showForgotPasswordForm() {
		return "forgot_password";
	}

	@GetMapping("/showVerify")
	public String showVerifyOTPForm() {
		return "verify_otp";
	}

	@PostMapping("/send-otp")
	public String sendOtp(@RequestParam("email") String email, RedirectAttributes redirectAttributes,
			HttpSession httpSession) {

		User user = userRepository.getUserByUserName(email);
		if (user == null) {
			redirectAttributes.addFlashAttribute("message",
					new Message("There is no such registered email address", "danger"));
			return "redirect:/forgot-password";
		} else {
			Random random = new Random();
			int otp = random.nextInt(9000) + 1000;
			httpSession.setAttribute("otp", otp);
			httpSession.setAttribute("email", email);
			String subject = "OTP for forgot password";
			String text = "<div style='border:1px solid #e2e2e2;padding:20px;'>" + "<h1>" + "OTP is " + "<b>" + otp
					+ "</b>" + "</h1>" + "<p>" + "Use this otp to verify your Email Address" + "</p>" + "</div>";
			boolean flag = emailService.sendEmail(subject, text, email);
			if (flag) {
				redirectAttributes.addFlashAttribute("message",
						new Message("We have sent an OTP to your Registered Email Address", "success"));
				return "redirect:/showVerify";
			} else {
				redirectAttributes.addFlashAttribute("message", new Message("Couldn't send OTP", "danger"));
				return "redirect:/forgot-password";
			}
		}
	}

	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession httpSession,
			RedirectAttributes redirectAttributes) {
		int sessionOtp = (int) httpSession.getAttribute("otp");
		if (sessionOtp == otp) {
			return "change_password";
		} else {
			redirectAttributes.addFlashAttribute("message", new Message("Invalid OTP", "danger"));
			return "redirect:/showVerify";
		}
	}

	@PostMapping("/update-password")
	public String updatePassword(@RequestParam("password") String password, HttpSession session,
			RedirectAttributes redirectAttributes) {
		String email = (String) session.getAttribute("email");
		User user = userRepository.getUserByUserName(email);
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
		redirectAttributes.addFlashAttribute("message", new Message("Password changed successfully", "success"));
		return "redirect:/signin";
	}
}
