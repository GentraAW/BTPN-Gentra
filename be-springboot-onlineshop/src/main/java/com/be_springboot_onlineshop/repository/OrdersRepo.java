package com.be_springboot_onlineshop.repository;
 
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.be_springboot_onlineshop.model.Orders;

public interface OrdersRepo extends JpaRepository<Orders, Long > {
    Optional<Orders> findByOrderId(Long orderId);
}

 

