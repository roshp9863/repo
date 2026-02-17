package com.inn.startandconnect.dao;

import org.springframework.stereotype.Repository;

import com.inn.startandconnect.model.Client;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
public class UserDao {
	
	
	private final EntityManager entityManager;

	public UserDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

	public Client findUserByEmailAndPassword(String email, String password) {
		
		String jpql = "SELECT u FROM Client u " + "WHERE u.email = :email and u.password = :password";

		Query query = entityManager.createQuery(jpql, Client.class);
		query.setParameter("email", email);
		query.setParameter("password", password);

		if(query.getResultList().size()>0) {
			return (Client)query.getResultList().get(0);
		}else {
			return null;
		}
		

	}



}
