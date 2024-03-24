package com.inn.startandconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.startandconnect.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {


}
