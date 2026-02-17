package com.inn.startandconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.startandconnect.model.Retailer;
import com.inn.startandconnect.service.RetailerService;


@RestController
@RequestMapping("/retailers")
public class RetailerController {

    @Autowired
    private RetailerService retailerService;

    
    //curl -X POST -H "Content-Type: application/json" -d '{"companyName":"Blinkit", "businessAddress":"Zomato ke pas", "contactNumber":"987654321"}' http://localhost:8080/retailers/createRetailer
    @PostMapping("/createRetailer")
    public Retailer createRetailer(@RequestBody Retailer retailer) {
        return retailerService.createRetailer(retailer);
    }
    
    @GetMapping("/allRetailers")
	public List<Retailer> getAllRetailers() {
		return retailerService.getAllRetailers();
	}

}
