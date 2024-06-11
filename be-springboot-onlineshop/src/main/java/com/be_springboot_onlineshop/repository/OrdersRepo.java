package com.be_springboot_onlineshop.repository;
 
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.be_springboot_onlineshop.model.Orders;

public interface OrdersRepo extends JpaRepository<Orders, Long > {
    Optional<Orders> findByOrderId(Long orderId);

    Page<Orders> findByCustomersCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);
}

 

