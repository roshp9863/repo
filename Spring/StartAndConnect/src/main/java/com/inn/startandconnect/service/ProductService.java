package com.inn.startandconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.startandconnect.model.Product;
import com.inn.startandconnect.model.Supplier;
import com.inn.startandconnect.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierService supplierService;


	public Product createProduct(Product product, Long id) {
		Supplier supplier = supplierService.getSupplierById(id);
		product.setSupplier(supplier);
		return productRepository.save(product);
		
	}


	public Product getProductById(Long productId) {
		Product p = productRepository.findById(productId).orElseThrow();
		return p;

	}
}
