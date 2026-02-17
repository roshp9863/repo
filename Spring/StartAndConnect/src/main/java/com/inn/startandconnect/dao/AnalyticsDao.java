package com.inn.startandconnect.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inn.startandconnect.model.Analytics;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
public class AnalyticsDao {
	
private final EntityManager entityManager;
    
    public AnalyticsDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public List<Analytics> getAnalyticsBySupplierId(Long supplierId) {
        String jpql = "SELECT a FROM Analytics a " +
                      "JOIN a.product p " +
                      "JOIN p.supplier s " +
                      "WHERE s.supplierId = :supplierId";
        
        Query query = entityManager.createQuery(jpql, Analytics.class);
        query.setParameter("supplierId", supplierId);
        
        return query.getResultList();
    }    

    public List<Analytics> getAnalyticsBySupplierAndProduct(Long supplierId, Long productId) {
        String jpql = "SELECT a FROM Analytics a " +
                      "JOIN a.product p " +
                      "JOIN p.supplier s " +
                      "WHERE s.supplierId = :supplierId and p.productId = :productId";
        
        Query query = entityManager.createQuery(jpql, Analytics.class);
        query.setParameter("supplierId", supplierId);
        query.setParameter("productId", productId);
        
        return query.getResultList();
    }    

    public List<Analytics> getAnalyticsBySupplierProductAndRetailer(Long supplierId, Long productId, Long retailerId) {
        String jpql = "SELECT a FROM Analytics a " +
                      "JOIN a.product p " +
                      "JOIN p.supplier s " +
                      "JOIN a.retailer r " +
                      "WHERE s.supplierId = :supplierId and p.productId = :productId and r.retailerId = :retailerId";
        
        Query query = entityManager.createQuery(jpql, Analytics.class);
        query.setParameter("supplierId", supplierId);
        query.setParameter("productId", productId);
        query.setParameter("retailerId", retailerId);
        
        return query.getResultList();
    }    

    
}
