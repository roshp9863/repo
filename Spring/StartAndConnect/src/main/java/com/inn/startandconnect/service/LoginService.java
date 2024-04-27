package com.inn.startandconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
	
    
    @Autowired
    private CustomUserService customerUserService;


	public Boolean login(String email, String password) {
		UserDetails c = customerUserService.loadUserByUsername(email);
		return c!=null ? true : false;
	}


	
}
