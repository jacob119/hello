package com.example.hello.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	static final String LOGIN_PAGE = "/login";

	private static final String CONTENT_SECURITY_POLICY = String.join("; ",
			"default-src 'self'",
			"style-src 'self' https://fonts.googleapis.com",
			"font-src https://fonts.gstatic.com",
			"img-src 'self' data:",
			"form-action 'self'",
			"frame-ancestors 'none'",
			"base-uri 'self'",
			"object-src 'none'");

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(LOGIN_PAGE, "/css/**", "/favicon.svg", "/error").permitAll()
				.requestMatchers("/api/hello", "/actuator/health").permitAll()
				.anyRequest().authenticated())
			.formLogin(form -> form
				.loginPage(LOGIN_PAGE)
				.defaultSuccessUrl("/", true))
			.logout(logout -> logout
				.logoutSuccessUrl(LOGIN_PAGE + "?logout"))
			.headers(headers -> headers
				.contentSecurityPolicy(csp -> csp.policyDirectives(CONTENT_SECURITY_POLICY)));
		return http.build();
	}

}
