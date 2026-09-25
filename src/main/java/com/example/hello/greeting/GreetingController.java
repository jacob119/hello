package com.example.hello.greeting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GreetingController {

	static final int MAX_NAME_LENGTH = 50;

	private final GreetingService greetingService;

	public GreetingController(GreetingService greetingService) {
		this.greetingService = greetingService;
	}

	@GetMapping("/hello")
	public Greeting hello(
			@RequestParam(defaultValue = "Jacob") @NotBlank @Size(max = MAX_NAME_LENGTH) String name) {
		return greetingService.greet(name);
	}

}
