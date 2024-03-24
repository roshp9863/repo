package com.inn.startandconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.startandconnect.model.User;

public interface UserRepository extends JpaRepository<User, Long> {


}
