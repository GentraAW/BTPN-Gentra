package com.be_springboot_onlineshop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.be_springboot_onlineshop.model.Customers;

@Repository
public interface CustomersRepo extends JpaRepository<Customers, Long> {
    Page<Customers> findByIsActive(boolean isActive, Pageable pageable);

    Optional<Customers> findByCustomerIdAndIsActive(Long customerId, boolean isActive);

    Page<Customers> findByCustomerNameContainingIgnoreCaseAndIsActive(String customerName, boolean isActive, Pageable pageable);
    
    boolean existsByCustomerPhone(String customerPhone);
}
