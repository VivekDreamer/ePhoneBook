package com.app.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.app.dao.UserRepository;
import com.app.entities.User;
import com.app.helper.Message;

@Controller
public class ControllerFirst {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "ePhoneBook");
		return "home";
	}
	
	//handler for opening signup form
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register-ePhoneBook");
		model.addAttribute("user", new User()); //sending blank user, and getting that data from there
		return "signup";
	}
	
	//handler for processing signup form	
	@RequestMapping(value="/do_register",method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,		
									BindingResult bindingResult,
									@RequestParam(value="agreement",defaultValue = "false") boolean agreement,
									Model model,
									@RequestParam("userimage") MultipartFile file,
									HttpSession session) {
		
		try {
			if(!agreement) {
				System.out.println("not checked terms and condition");
				throw new Exception("please, agree terms and conditions.");
			}	
			
//			System.out.println(file.getName());
//			System.out.println(file.getOriginalFilename());
//			System.out.println(file.getName());
//			System.out.println(file.getSize());
			
			if(bindingResult.hasErrors()) {
//				System.out.println("error ---------->"+bindingResult.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			
			if (file.isEmpty()) {
				// if file is empty
				user.setImageUrl("userImage.jpg");
			} else {
				// uploading file to folder and update the name in contact
				user.setImageUrl((new Date()).getTime() + file.getOriginalFilename());
				File savedImage = new ClassPathResource("static/USER_PROFILE").getFile();
//				System.out.println("before path");
				Path path = Paths.get(
						savedImage.getAbsolutePath() + File.separator + (new Date()).getTime() + file.getOriginalFilename());
//				System.out.println("After path");
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//				System.out.println("image is uploaded!!!");
			}
			user.setPassword(passwordEncoder.encode(user.getPassword()));			
//			System.out.println("agreement "+agreement);
//			System.out.println("User "+user);
	    	model.addAttribute("user", user);
			User result = this.userRepository.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully registered","alert-success"));
			return "signup";
			
		} catch (Exception e) {
			
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong!!"+e.getMessage(),"alert-danger"));
			return "signup";
		}
	}
	
	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title", "login page!!!");
		return "login";
	}
	
}
