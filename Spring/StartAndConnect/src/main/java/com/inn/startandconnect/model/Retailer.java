package com.inn.startandconnect.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Retailer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long retailerId;
    private String companyName;
    private String businessAddress;
    private String contactNumber;

    public Retailer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Retailer(Long retailerId, String companyName, String businessAddress, String contactNumber) {
		super();
		this.retailerId = retailerId;
		this.companyName = companyName;
		this.businessAddress = businessAddress;
		this.contactNumber = contactNumber;
	}

	public Long getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Long retailerId) {
		this.retailerId = retailerId;
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

	@Override
	public String toString() {
		return "Retailer [retailerId=" + retailerId + ", companyName=" + companyName + ", businessAddress="
				+ businessAddress + ", contactNumber=" + contactNumber + "]";
	}
    
	
    
}
