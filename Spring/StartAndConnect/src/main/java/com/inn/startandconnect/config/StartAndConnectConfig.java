package com.inn.startandconnect.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

//@Configuration
public class StartAndConnectConfig {

	
//	@Bean
//	public DataSource dataSource() {
//		DriverManagerDataSource dm = new DriverManagerDataSource();
//		dm.setUsername("root");
//		dm.setPassword("Ap@123456");
//		dm.setUrl("jdbc:mysql://localhost:3306/StartAndConnect");
//		dm.setDriverClassName("com.mysql.cj.jdbc.Driver");
//		return dm;
//	}
//	
//	@Bean
//	public JdbcTemplate jdbcTemplate(DataSource ds) {
//		return new JdbcTemplate(ds);
//	}
//	
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder(); 
//	}
}
 