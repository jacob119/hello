package com.example.hello.greeting;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.hello.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(GreetingController.class)
@Import({ GreetingService.class, SecurityConfig.class })
class GreetingControllerTest {

	@Autowired
	private MockMvcTester mvc;

	@Test
	void returnsDefaultGreetingWhenNameIsMissing() {
		assertThat(mvc.get().uri("/api/hello"))
			.hasStatusOk()
			.bodyJson()
			.extractingPath("$.message")
			.isEqualTo("Hello. Jacob");
	}

	@Test
	void returnsGreetingForGivenName() {
		assertThat(mvc.get().uri("/api/hello").param("name", "Tom"))
			.hasStatusOk()
			.bodyJson()
			.extractingPath("$.message")
			.isEqualTo("Hello. Tom");
	}

	@Test
	void rejectsBlankName() {
		assertThat(mvc.get().uri("/api/hello").param("name", "   "))
			.hasStatus(HttpStatus.BAD_REQUEST);
	}

	@Test
	void rejectsNameLongerThanMaxLength() {
		assertThat(mvc.get().uri("/api/hello").param("name", "a".repeat(GreetingController.MAX_NAME_LENGTH + 1)))
			.hasStatus(HttpStatus.BAD_REQUEST);
	}

}
