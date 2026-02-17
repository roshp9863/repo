package com.inn.startandconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.startandconnect.model.Analytics;
import com.inn.startandconnect.service.AnalyticsService;


@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @PostMapping("/createAnalytics/{supplierId}/{productId}/{retailerId}")
    public Analytics createAnalytics(
    		@RequestBody Analytics analytics, 
    		@PathVariable Long supplierId, 
    		@PathVariable Long productId, 
    		@PathVariable Long retailerId) {
        return analyticsService.createAnalytics(analytics, supplierId, productId, retailerId);
    }
    
    @GetMapping("/getAnalyticsBySupplier/{supplierId}")
    public List<Analytics> getAnalyticsBySupplier(@PathVariable Long supplierId) {
    	List<Analytics> a = analyticsService.getAnalyticsBySupplier(supplierId);
    	return a;
    	
    }

    @GetMapping("/getAnalyticsBySupplierAndProduct/{supplierId}/{productId}")
    public List<Analytics> getAnalyticsBySupplierAndProduct(@PathVariable Long supplierId, @PathVariable Long productId) {
    	List<Analytics> a = analyticsService.getAnalyticsBySupplierAndProduct(supplierId, productId);
    	return a;
    	
    }

    @GetMapping("/getAnalyticsBySupplierProductAndRetailer/{supplierId}/{productId}/{retailerId}")
    public List<Analytics> getAnalyticsBySupplierAndProduct(
    		@PathVariable Long supplierId, 
    		@PathVariable Long productId, 
    		@PathVariable Long retailerId) {
    	List<Analytics> a = analyticsService.getAnalyticsBySupplierProductAndRetailer(supplierId, productId, retailerId);
    	return a;
    	
    }

}
