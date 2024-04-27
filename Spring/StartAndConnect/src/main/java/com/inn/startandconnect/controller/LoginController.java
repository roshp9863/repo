package com.inn.startandconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inn.startandconnect.service.LoginService;

@RestController
@RequestMapping("/startandconnect")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@GetMapping("/welcome")
	public String welcomeToStartAndConnet() {
		return "WELCOME TO START AND CONNECT";
	}

	@PostMapping("/login")
	public Boolean login(@RequestParam String email, @RequestParam String password) {
		
		if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
			return loginService.login(email, password);
		}else
			throw new RuntimeException("Invalid Email or Password");
	}

}
