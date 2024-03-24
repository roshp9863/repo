package com.inn.startandconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.startandconnect.model.Product;
import com.inn.startandconnect.service.ProductService;



@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    
    @PostMapping("/createProduct/{supplierId}")
    public Product createProduct(@RequestBody Product product, @PathVariable Long supplierId) {
        return productService.createProduct(product, supplierId);
    }
}