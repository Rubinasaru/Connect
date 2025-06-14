package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.User.User;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String HomePage(Model model) {
		model.addAttribute("User",new User());
		return "home page";
	}
}
