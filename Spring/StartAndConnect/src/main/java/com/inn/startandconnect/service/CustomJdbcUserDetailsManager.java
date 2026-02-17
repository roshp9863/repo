package com.inn.startandconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.inn.startandconnect.model.Client;
import com.inn.startandconnect.repository.UserRepository;

@Service
public class CustomJdbcUserDetailsManager { //implements UserDetailsManager {

//    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;


    @Autowired
    public CustomJdbcUserDetailsManager(PasswordEncoder passwordEncoder) {
//        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(Client userDetails) {
        String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
//        jdbcTemplate.update("INSERT INTO user (username, password_hash) VALUES (?, ?)",
//                userDetails.getUsername(), encodedPassword);
        userDetails.setPassword(encodedPassword);
        userRepository.save(userDetails);
        
        
    }

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void updateUser(UserDetails user) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void deleteUser(String username) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void changePassword(String oldPassword, String newPassword) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public boolean userExists(String username) {
//		// TODO Auto-generated method stub
//		return false;
//	}

    // Implement other methods of UserDetailsManager interface such as updateUser, deleteUser, etc.
}
