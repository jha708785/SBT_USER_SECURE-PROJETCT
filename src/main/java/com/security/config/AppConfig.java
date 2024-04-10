package com.security.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.security.jwt.JwtValidator;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class AppConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(Authorize ->

				Authorize.requestMatchers(HttpMethod.POST,
                         "/api/v1/auth/register/**",
                         "/api/v1/auth/refresh-token",
                         "/api/v1/auth/enable-user/**",
                         "/api/v1/auth/authenticate",
                         "/api/v1/auth/forgot-password",
                         "/api/v1/auth/reset-password")
                 .permitAll()
				.requestMatchers(HttpMethod.GET, "/api/v1/user/**")
				.hasAuthority("ROLE_USER")
				.requestMatchers(HttpMethod.GET, "/api/v1/admin/**")
				.hasAuthority("ROLE_ADMIN")
				.anyRequest()
				.authenticated())
				.addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class).csrf().disable().cors()
				.configurationSource(new CorsConfigurationSource() {

					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration cfg = new CorsConfiguration();

						cfg.setAllowedOrigins(Arrays.asList(

								"http://localhost:3300", "http://localhost:4200"));

						cfg.setAllowedMethods(Collections.singletonList("*"));
						cfg.setAllowCredentials(true);
						cfg.setAllowedHeaders(Collections.singletonList("*"));
						cfg.setExposedHeaders(Arrays.asList("Authorization"));
						cfg.setMaxAge(3600L);

						return cfg;
					}
				}).and().httpBasic().and().formLogin();
		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
