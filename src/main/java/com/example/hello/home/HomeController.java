package com.example.hello.home;

import java.security.Principal;

import com.example.hello.greeting.GreetingService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	private final GreetingService greetingService;

	public HomeController(GreetingService greetingService) {
		this.greetingService = greetingService;
	}

	@GetMapping("/")
	public String home(Principal principal, Model model) {
		model.addAttribute("greeting", greetingService.greet(principal.getName()));
		model.addAttribute("username", principal.getName());
		return "home";
	}

}
