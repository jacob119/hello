package com.example.hello.greeting;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

	private static final String TEMPLATE = "Hello. %s";

	public Greeting greet(String name) {
		return new Greeting(TEMPLATE.formatted(name.strip()));
	}

}
