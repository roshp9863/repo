package com.inn.startandconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;

import com.inn.startandconnect.model.Client;
import com.inn.startandconnect.model.UserRequest;
import com.inn.startandconnect.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/createClient")
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client user) {
        logger.info("Creating new client: {}", user);
        Client createdClient = userService.createClient(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    @PostMapping("/createUserWithSupplier")
//    @PreAuthorize("permitAll()")
    public ResponseEntity<String> createUserWithSupplier(@Valid @RequestBody UserRequest userRequest) {
        logger.info("Creating user with supplier: {}", userRequest);
        String result = userService.createUserWithSupplier(userRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<Client>> getAllUsers() {
        logger.info("Fetching all users");
        List<Client> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
