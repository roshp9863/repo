package com.inn.startandconnect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.startandconnect.model.User;
import com.inn.startandconnect.model.Supplier;
import com.inn.startandconnect.model.UserRequest;
import com.inn.startandconnect.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupplierService supplierService;

    
    public User createUser(User user) {
        return userRepository.save(user);
    }

	public User createUserWithSupplier(UserRequest request) {

		User user = request.getUser();
		Supplier supplier = request.getSupplier();
		
		Supplier savedSupplier = supplierService.createSupplier(supplier);
		
		user.setSupplier(savedSupplier);
		return createUser(user);
		
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	
}
