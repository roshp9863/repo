package com.inn.startandconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.startandconnect.model.Supplier;
import com.inn.startandconnect.model.Analytics;
import com.inn.startandconnect.model.Product;
import com.inn.startandconnect.model.Retailer;
import com.inn.startandconnect.repository.AnalyticsRepository;

@Service
public class AnalyticsService {

    @Autowired
    private AnalyticsRepository analyticsRepository;
    
    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RetailerService retailerService;


    public Analytics createAnalytics(Analytics analytics, Long supplierId, Long productId, Long retailerId) {
    	
    	Supplier supplier = supplierService.getSupplierById(supplierId);
    	Product product = productService.getProductById(productId);
    	Retailer retailer = retailerService.getRetailerById(retailerId);
    	
    	analytics.setProduct(product);
    	analytics.setRetailer(retailer);
    	analytics.setSupplier(supplier);
    	
        return analyticsRepository.save(analytics);
    }
}
