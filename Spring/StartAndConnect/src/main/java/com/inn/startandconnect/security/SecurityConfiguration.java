package com.inn.startandconnect.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//import com.inn.startandconnect.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

//	private final UserService userService;

	// {noop} work for nooppasswordencoder
//	@Bean
//	public InMemoryUserDetailsManager setUpUser() {
//		UserDetails ud = User.withUsername("Rosh").password("{noop}SAndC").roles("admin").build();
////		UserDetails ud1 = User.withUsername("Jass")
////				.password("{bcrypt}$2a$10$ehxkbKNttSQIXeIlDYugZ.YDjqgmhSs0BVXXwoggqJ4aT.Flq/J6e").roles("user").build();
//
//		
//		InMemoryUserDetailsManager udm = new InMemoryUserDetailsManager();
//		udm.createUser(ud);
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
//	public SecurityFilterChain settingUpHttpSecurity(HttpSecurity http) throws Exception {
//		
//		http.authorizeHttpRequests(c -> {
////			c.requestMatchers("/").permitAll();
//			c.requestMatchers("/users/createUserWithSupplier").permitAll();
//			c.requestMatchers("/startandconnect/welcome").permitAll();
//			
//		});
//		http.formLogin();
//		http.httpBasic();
////		http.authorizeRequests().requestMatchers("/startandconnect/login").permitAll().anyRequest()
////				.authenticated();
//		http.csrf().disable();
//		return http.build();
//
//	}
	
	
	
	@Bean
	public SecurityFilterChain settingUpHttpSecurity(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(authorizeRequests ->
	            authorizeRequests
	                .requestMatchers("/users/createUserWithSupplier").permitAll()
	                .requestMatchers("/startandconnect/login").permitAll()
//	                .requestMatchers("/startandconnect/welcome").permitAll()
//	                .requestMatchers("/products/**").permitAll()
//	                .requestMatchers("/analytics/**").permitAll()
//	                .requestMatchers("/retailers/**").permitAll()
	                .anyRequest().authenticated()
	        )
	        .formLogin()
	            .permitAll().defaultSuccessUrl("/startandconnect/welcome") // Allow access to the login page
	        .and()
	        .httpBasic(); // Enable HTTP basic authentication
	    http.csrf().disable(); // Disable CSRF protection
	    return http.build();
	}

	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dm = new DriverManagerDataSource();
		dm.setUsername("root");
		dm.setPassword("Ap@123456");
		dm.setUrl("jdbc:mysql://localhost:3306/StartAndConnect");
		dm.setDriverClassName("com.mysql.cj.jdbc.Driver");
		return dm;
	}
	
//	@Bean
//	public JdbcUserDetailsManager JdbcUserDetailManager() {
//		return new JdbcUserDetailsManager(dataSource());
//	}
//	
//	@Bean
//	public JdbcTemplate jdbcTemplate(DataSource ds) {
//		return new JdbcTemplate(ds);
//	}
	
	@Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authentication -> authentication;
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); 
	}

}
