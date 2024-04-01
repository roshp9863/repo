package com.inn.startandconnect.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;

//import com.inn.startandconnect.service.UserService;

public class SecurityConfiguration {

//	private final UserService userService;

	// {noop} work for nooppasswordencoder
//	@Bean
//	public InMemoryUserDetailsManager setUpUser() {
//		UserDetails ud = User.withUsername("Rosh").password("{noop}SAndC").roles("admin","user").build();
//		UserDetails ud1 = User.withUsername("Jass")
//				.password("{bcrypt}$2a$10$ehxkbKNttSQIXeIlDYugZ.YDjqgmhSs0BVXXwoggqJ4aT.Flq/J6e").roles("user").build();
//
//		
//		InMemoryUserDetailsManager udm = new InMemoryUserDetailsManager();
//		udm.createUser(ud);
//		udm.createUser(ud1);
//		return udm;
//	}

	// if we are encode the passowrd then we are getting error
	// java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for
	// the id "null"
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
//	}

//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder(); 
//	}

//	@Bean
//	public UserDetailsService userDetailsService() {
//		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//	    manager.createUser(User.withUsername("user")
//	      .password("userPass")
//	      .roles("USER")
//	      .build());
//	    manager.createUser(User.withUsername("admin")
//	      .password("adminPass")
//	      .roles("USER", "ADMIN")
//	      .build());
//	    return manager;
//	}

//	public SecurityConfiguration(UserService userService) {
//		this.userService = userService;
//	}
//
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
//	}
//
//	public UserService getUserService() {
//		return userService;
//	}
//
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.authorizeRequests().requestMatchers("/users/createUserWithSupplier").permitAll().anyRequest()
//				.authenticated();
//		http.csrf().disable();
//		return http.build();

//	}
}
