package com.inn.startandconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.startandconnect.model.Supplier;
import com.inn.startandconnect.repository.SupplierRepository;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

	public Supplier getSupplierById(Long id) {
		Supplier s = supplierRepository.findById(id).orElseThrow();
		return s;
	}
}
