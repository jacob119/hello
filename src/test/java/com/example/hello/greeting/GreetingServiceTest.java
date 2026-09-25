package com.example.hello.greeting;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GreetingServiceTest {

	private final GreetingService service = new GreetingService();

	@Test
	void greetsGivenName() {
		Greeting greeting = service.greet("Jacob");

		assertThat(greeting.message()).isEqualTo("Hello. Jacob");
	}

	@Test
	void trimsSurroundingWhitespace() {
		Greeting greeting = service.greet("  Jacob  ");

		assertThat(greeting.message()).isEqualTo("Hello. Jacob");
	}

}
