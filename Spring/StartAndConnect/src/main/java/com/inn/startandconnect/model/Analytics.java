package com.inn.startandconnect.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analyticsId;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "retailer_id")
    private Retailer retailer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private Double unitPrice;
    private Date transactionDate;

    public Analytics() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Analytics(Long analyticsId, Supplier supplier, Retailer retailer, Product product, Integer quantity,
			Double unitPrice, Date transactionDate) {
		super();
		this.analyticsId = analyticsId;
		this.supplier = supplier;
		this.retailer = retailer;
		this.product = product;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.transactionDate = transactionDate;
	}

	public Long getAnalyticsId() {
		return analyticsId;
	}

	public void setAnalyticsId(Long analyticsId) {
		this.analyticsId = analyticsId;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Retailer getRetailer() {
		return retailer;
	}

	public void setRetailer(Retailer retailer) {
		this.retailer = retailer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public String toString() {
		return "Analytics [analyticsId=" + analyticsId + ", supplier=" + supplier + ", retailer=" + retailer
				+ ", product=" + product + ", quantity=" + quantity + ", unitPrice=" + unitPrice + ", transactionDate="
				+ transactionDate + "]";
	}
    
    
}
