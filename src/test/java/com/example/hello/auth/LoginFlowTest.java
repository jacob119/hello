package com.example.hello.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest(properties = {
		"spring.security.user.name=" + LoginFlowTest.USERNAME,
		"spring.security.user.password=" + LoginFlowTest.PASSWORD })
@AutoConfigureMockMvc
class LoginFlowTest {

	static final String USERNAME = "jacob";

	static final String PASSWORD = "test-password";

	@Autowired
	private MockMvcTester mvc;

	@Test
	void loginPageIsPublic() {
		assertThat(mvc.get().uri("/login"))
			.hasStatusOk()
			.bodyText()
			.contains("<form", "name=\"username\"", "name=\"password\"", "name=\"_csrf\"");
	}

	@Test
	void loginPageShowsErrorAfterFailedAttempt() {
		assertThat(mvc.get().uri("/login").param("error", ""))
			.hasStatusOk()
			.bodyText()
			.contains("아이디 또는 비밀번호가 올바르지 않습니다");
	}

	@Test
	void loginPageShowsNoticeAfterLogout() {
		assertThat(mvc.get().uri("/login").param("logout", ""))
			.hasStatusOk()
			.bodyText()
			.contains("로그아웃되었습니다");
	}

	@Test
	void homeRedirectsAnonymousUserToLogin() {
		assertThat(mvc.get().uri("/"))
			.hasStatus3xxRedirection()
			.redirectedUrl()
			.endsWith("/login");
	}

	@Test
	void validCredentialsRedirectToHome() {
		assertThat(mvc.perform(formLogin("/login").user(USERNAME).password(PASSWORD)))
			.hasStatus3xxRedirection()
			.hasRedirectedUrl("/");
	}

	@Test
	void invalidCredentialsRedirectBackWithError() {
		assertThat(mvc.perform(formLogin("/login").user(USERNAME).password("wrong")))
			.hasStatus3xxRedirection()
			.hasRedirectedUrl("/login?error");
	}

	@Test
	@WithMockUser(username = "Tom")
	void homeGreetsAuthenticatedUser() {
		assertThat(mvc.get().uri("/"))
			.hasStatusOk()
			.bodyText()
			.contains("Hello. Tom", "action=\"/logout\"");
	}

	@Test
	@WithMockUser
	void logoutRedirectsToLoginWithNotice() {
		assertThat(mvc.perform(logout()))
			.hasStatus3xxRedirection()
			.hasRedirectedUrl("/login?logout");
	}

	@Test
	void greetingApiStaysPublic() {
		assertThat(mvc.get().uri("/api/hello")).hasStatusOk();
	}

	@Test
	void healthEndpointStaysPublic() {
		assertThat(mvc.get().uri("/actuator/health")).hasStatusOk();
	}

	@Test
	void responsesCarryContentSecurityPolicy() {
		assertThat(mvc.get().uri("/login"))
			.headers()
			.hasHeaderSatisfying("Content-Security-Policy",
					values -> assertThat(values.getFirst()).contains("default-src 'self'", "frame-ancestors 'none'"));
	}

}
