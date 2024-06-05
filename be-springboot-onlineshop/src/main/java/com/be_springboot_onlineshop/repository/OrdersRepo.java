package com.be_springboot_onlineshop.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;

import com.be_springboot_onlineshop.model.Orders;

public interface OrdersRepo extends JpaRepository<Orders, Long > {

    
}

 

