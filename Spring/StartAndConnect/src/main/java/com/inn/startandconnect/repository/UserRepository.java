package com.inn.startandconnect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.startandconnect.model.Client;

public interface UserRepository extends JpaRepository<Client, Long> {
	
	Client findByUsername(String username);

	Client findByEmail(String email);

	Client findUserByEmailAndPassword(String email, String password);

}
