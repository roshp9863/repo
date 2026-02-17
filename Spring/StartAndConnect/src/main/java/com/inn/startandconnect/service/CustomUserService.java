package com.inn.startandconnect.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.inn.startandconnect.dto.ClientDTO;
import com.inn.startandconnect.model.Client;

@Service
public class CustomUserService  implements UserDetailsService {
	
	@Autowired
	UserService userService;
	
	@Autowired
    private AuthenticationManager authenticationManager;

	
	
	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = userService.findByEmail(email);
        if (client == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(
                client.getUsername(), client.getPassword(), true, true, true, true,
                AuthorityUtils.createAuthorityList("ROLE_USER")); // Customize roles as needed
    }
	
	public void authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, password, userDetails.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
