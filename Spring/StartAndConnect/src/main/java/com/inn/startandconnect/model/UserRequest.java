package com.inn.startandconnect.model;

public class UserRequest {
    private Supplier supplier;
    private Client user;
    
    public UserRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

    public UserRequest(Supplier supplier, Client user) {
		super();
		this.supplier = supplier;
		this.user = user;
	}

    public Supplier getSupplier() {
		return supplier;
	}
	
    public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	
    public Client getUser() {
		return user;
	}
	
    public void setUser(Client user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "SupplierRequest [supplier=" + supplier + ", user=" + user + "]";
	}

    
    
}
