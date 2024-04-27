package com.inn.startandconnect.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inn.startandconnect.dao.UserDao;
import com.inn.startandconnect.model.Client;
import com.inn.startandconnect.model.Supplier;
import com.inn.startandconnect.model.UserRequest;
import com.inn.startandconnect.repository.UserRepository;

@Service
public class UserService  {//implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDao userDao;

    
    @Autowired
    private SupplierService supplierService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public Client createClient(Client user) {
        return userRepository.save(user);
    }
    
	public String createUserWithSupplier(UserRequest request) {

		Client user = request.getUser();
		
		Boolean isNotPresent = isUserAlreadyInDB(user);
		
		if(isNotPresent) {
		
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			Supplier supplier = request.getSupplier();
			
			Supplier savedSupplier = supplierService.createSupplier(supplier);
			
			user.setSupplier(savedSupplier);
			createClient(user);
			return "USER CREATED SUCCESSFULLY";
		}else {
			throw new RuntimeException("User is already Present");
		}

		
		
	}

	private Boolean isUserAlreadyInDB(Client user) {
		Client c =null;
		c = findByEmail(user.getEmail());
		return c==null ? true : false;
	}

	public List<Client> getAllUsers() {
		return userRepository.findAll();
	}

	public Client findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public Client findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Client findUserByEmailAndPassword(String email, String password) {
		return userDao.findUserByEmailAndPassword(email, password);
	}

	
	
	
	
}
