package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig {
	@Bean
	public UserDetailsService getUserDetailService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getUserDetailService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
	/*
	 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
	 * Exception { http.authenticationProvider(authenticationProvider());
	 * http.authorizeRequests().requestMatchers("/admin/**").hasRole("ADMIN").
	 * requestMatchers("/user/**").hasRole("USER").requestMatchers("/**").permitAll(
	 * ).and().formLogin(Customizer.withDefaults()).csrf().disable(); return
	 * http.build(); }
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.authenticationProvider(authenticationProvider())
	        .authorizeRequests()
	            .requestMatchers("/admin/**").hasRole("ADMIN")
	            .requestMatchers("/user/**").hasRole("USER")
	            .requestMatchers("/**").permitAll()
	        .and()
	            .formLogin()
	                .loginPage("/signin")
	                .permitAll() 
	                .loginProcessingUrl("/dologin")
	                .defaultSuccessUrl("/user/index")
	        .and()
	            .csrf().disable();

	    return http.build();
	}

	
	
	
	
}
