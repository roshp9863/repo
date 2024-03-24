package com.inn.startandconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public User createUserWithSupplier(@RequestBody UserRequest userRequest) {
        return userService.createUserWithSupplier(userRequest);
    }

}
