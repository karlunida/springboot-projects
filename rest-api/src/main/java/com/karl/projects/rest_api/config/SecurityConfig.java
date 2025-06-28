package com.karl.projects.rest_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults())
			.csrf( csrf -> csrf.disable())
			.userDetailsService(userDetailsService())
			.authorizeHttpRequests(req -> req.requestMatchers("/employee/**").permitAll()  //allow all request for this test
					.anyRequest().permitAll())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

		;
		return http.build();
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		var user = User.withUsername("user").password("password").roles("USER").passwordEncoder(passwordEncoder()::encode).build();
		return new InMemoryUserDetailsManager(user);
	}

}
