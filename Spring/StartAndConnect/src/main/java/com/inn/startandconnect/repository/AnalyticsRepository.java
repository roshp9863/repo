package com.inn.startandconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inn.startandconnect.model.Analytics;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {

	List<Analytics> findByProduct_Supplier_SupplierId(Long supplierId);
}
