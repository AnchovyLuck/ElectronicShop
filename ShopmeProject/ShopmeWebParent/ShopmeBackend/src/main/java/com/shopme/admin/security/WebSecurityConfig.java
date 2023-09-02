package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public UserDetailsService userDetailService() {
		return new ShopmeUserDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(authz -> authz.requestMatchers("/images/**", "/js/**", "/webjars/**", "/**").permitAll()
				.anyRequest().authenticated())
				.formLogin(formLogin -> formLogin.loginPage("/login").usernameParameter("email").permitAll())
				.authenticationProvider(authenticationProvider()).logout(logout -> logout.permitAll())
				.rememberMe(remember -> remember.key("AbcDefgHijKlmnOpqrs_0123456789").tokenValiditySeconds(7 * 24 * 3600));

		return http.build();
	}
}
