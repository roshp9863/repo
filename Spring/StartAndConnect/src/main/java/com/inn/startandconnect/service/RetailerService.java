package com.inn.startandconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.startandconnect.model.Retailer;
import com.inn.startandconnect.repository.RetailerRepository;

@Service
public class RetailerService {

    @Autowired
    private RetailerRepository retailerRepository;

    public Retailer createRetailer(Retailer retailer) {
        return retailerRepository.save(retailer);
    }

	public Retailer getRetailerById(Long retailerId) {
		Retailer r = retailerRepository.findById(retailerId).orElseThrow();
		return r;
	}
}
