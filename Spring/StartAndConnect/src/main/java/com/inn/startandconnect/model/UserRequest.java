package com.inn.startandconnect.model;

public class UserRequest {
    private Supplier supplier;
    private User user;
    
    public UserRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

    public UserRequest(Supplier supplier, User user) {
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
	
    public User getUser() {
		return user;
	}
	
    public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "SupplierRequest [supplier=" + supplier + ", user=" + user + "]";
	}

    
    
}
