package com.inn.startandconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
}
