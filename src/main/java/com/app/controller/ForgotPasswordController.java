package com.app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.dao.UserRepository;
import com.app.entities.User;
import com.app.service.EmailService;

@Controller
public class ForgotPasswordController {
	@Autowired
	private EmailService emailService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	//form that will take email id as input when you click forget password
	@RequestMapping("/forgot")
	public String emailForm() {
		return "forgetEmailForm";
	}
	
	//here, user will enter the otp sent to his email
	@PostMapping("/send-otp")
	public String sendOtpForm(@RequestParam("emailName") String email,HttpSession session) {
		
		//getting user from email entered
		User userEmail = userRepository.getUserByUserName(email);
		if(userEmail == null) {
			session.setAttribute("message", "Sorry! We don't have user registered with this email.");
			return "forgetEmailForm";
		}
		
		//generating 6 digit OTP
		int min = 100000;
		int max = 999999;
		int otp = (int)(Math.random()*(max-min+1)+min);  
		System.out.println("otp : "+otp);
		
		
		//writing code for sending email
		String subject = "OTP from ePhoneBook";
		String message = "<div style='border:1px solid #e2e2e2;'padding: 30px;'>"
				+ "<h1>"
				+ "OTP is :"
				+ "<b style='color:blue;'>"+otp
				+ "  </b>"
				
				+ "</h1>"
				+ "</div>";		
		String to = email;
		boolean result = this.emailService.sendEmail(subject, message, to);
		if(result) {
			session.setAttribute("otpGenerated", otp);
			session.setAttribute("email", email);
			return "verifyOTP";
		}
		//I think, this is not needed right now
		else {
			session.setAttribute("message", "check your email id...");
			return "forgetEmailForm";
		}
		
	}
	
	//verify OTP
	@PostMapping("/verify-otp")
	public String verifyOTP(@RequestParam("otp") int otp, HttpSession session) {
		
		int otpGenerated = (int) session.getAttribute("otpGenerated");
		System.out.println("OTP entered : " + otp);
		System.out.println("OTP generated : "+ otpGenerated);
		System.out.println(otp==otpGenerated);
		String email = (String) session.getAttribute("email");
		if(otp == otpGenerated) {
			//return password change form
			
			return "passwordChangeForm";
		}
		else {
			//go back to verify otp
			session.setAttribute("message", "Please enter right OTP...");
			return "verifyOTP";

		}
		
	}
	
	//reset password controller
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam("newpassword") String newPassword, @RequestParam("newpassword1") String newPassword1, HttpSession session) {
		String email = (String) session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		
		if(newPassword.equals(newPassword1)) {
			user.setPassword(this.bcryptPasswordEncoder.encode(newPassword1));
			this.userRepository.save(user);
			return "redirect:/signin?change=password changed successfully!!";
		}
		else {
			session.setAttribute("message", "Please enter password correctly!!!");
			return "passwordChangeForm";
		}
	}
}
