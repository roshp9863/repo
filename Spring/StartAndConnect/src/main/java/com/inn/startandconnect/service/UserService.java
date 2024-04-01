package com.inn.startandconnect.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User.UserBuilder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inn.startandconnect.model.User;
import com.inn.startandconnect.model.Supplier;
import com.inn.startandconnect.model.UserRequest;
import com.inn.startandconnect.repository.UserRepository;

@Service
public class UserService {//implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupplierService supplierService;
    
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    

	public User createUserWithSupplier(UserRequest request) {

		User user = request.getUser();
//		user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
		Supplier supplier = request.getSupplier();
		
		Supplier savedSupplier = supplierService.createSupplier(supplier);
		
		user.setSupplier(savedSupplier);
		return createUser(user);
		
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		Optional<User> user = userRepository.findByUsername(username);
//        UserBuilder builder = null;
//        if (user.isPresent()) {
//            User currentUser = user.get();
//            builder = org.springframework.security.core.userdetails.
//                      User.withUsername(username);
//            builder.password(currentUser.getPasswordHash());
////            builder.roles(currentUser.getRole());
//        } else {
//            throw new UsernameNotFoundException("User not found.");
//        }
//        return builder.build();	
//    }
	
	
//	@Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		User user = userRepository.findByUsername(username).get();
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//        return new org.springframework.security.core.userdetails.User(
//            user.getUsername(), user.getPasswordHash(), Collections.emptyList());
//    }
	
}
