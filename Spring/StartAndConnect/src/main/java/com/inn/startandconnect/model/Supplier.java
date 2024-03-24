package com.inn.startandconnect.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;


@Entity
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;
    private String companyName;
    private String businessAddress;
    private String contactNumber;

    @OneToMany(mappedBy = "supplier")
    private List<Product> products;

	public Supplier() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Supplier(Long supplierId, String companyName, String businessAddress, String contactNumber,
			List<Product> products) {
		super();
		this.supplierId = supplierId;
		this.companyName = companyName;
		this.businessAddress = businessAddress;
		this.contactNumber = contactNumber;
		this.products = products;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Supplier [supplierId=" + supplierId + ", companyName=" + companyName + ", businessAddress="
				+ businessAddress + ", contactNumber=" + contactNumber + ", products=" + products + "]";
	}
    
    
}
