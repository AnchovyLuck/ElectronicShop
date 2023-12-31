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
	UserDetailsService userDetailService() {
		return new ShopmeUserDetailsService();
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(authz -> authz.requestMatchers("/users/**", "/settings/**", "/countries/", "/states/").hasAuthority("Admin")
				.requestMatchers("/categories/**", "/brands/**", "/articles/**", "/menus/**").hasAnyAuthority("Admin", "Editor")
				
				.requestMatchers("/products/edit/**", "products/save", "/products/check_unique").hasAnyAuthority("Admin", "Editor", "Salesperson")
				
				.requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**", "products/editPrice", "products/savePrice").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
				
				.requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
				
				.requestMatchers("/customers/**", "/shipping/**", "/report/**").hasAnyAuthority("Admin", "Salesperson")
				.requestMatchers("/orders/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
				.requestMatchers("/images/**", "/js/**", "/webjars/**", "style.css", "/fontawesome/**").permitAll().anyRequest().authenticated())
				.formLogin(formLogin -> formLogin.loginPage("/login").usernameParameter("email").permitAll())
				.authenticationProvider(authenticationProvider()).logout(logout -> logout.permitAll()).rememberMe(
						remember -> remember.key("AbcDefgHijKlmnOpqrs_0123456789").tokenValiditySeconds(7 * 24 * 3600));

		return http.build();
	}
}