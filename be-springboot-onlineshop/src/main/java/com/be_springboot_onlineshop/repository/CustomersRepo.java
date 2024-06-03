package com.be_springboot_onlineshop.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.be_springboot_onlineshop.model.Customers;

@Repository
public interface CustomersRepo extends JpaRepository<Customers, UUID>{
    List<Customers> findByIsActive(boolean isActive);
    Optional<Customers> findByCustomerIdAndIsActive(UUID customerId, boolean isActive);
}
