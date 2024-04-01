package com.inn.startandconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.startandconnect.model.User;
import com.inn.startandconnect.model.UserRequest;
import com.inn.startandconnect.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/createUserWithSupplier")
//    @PreAuthorize("permitAll()")
    public User createUserWithSupplier(@RequestBody UserRequest userRequest) {
        return userService.createUserWithSupplier(userRequest);
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
