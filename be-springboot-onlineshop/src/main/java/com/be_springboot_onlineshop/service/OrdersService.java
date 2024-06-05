
package com.be_springboot_onlineshop.service;
 
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.be_springboot_onlineshop.model.Orders;
import com.be_springboot_onlineshop.repository.OrdersRepo;

@Service
 public class OrdersService {
     
    @Autowired
    private OrdersRepo ordersRepo;
    
    public List<Orders> getAllOrders() {        return ordersRepo.findAll();
    }
    
   public Optional<Orders> getOrderById(Long orderId) {
        return ordersRepo.findById(orderId);
    }
    
    public Orders createOrder(Orders newOrder) {
        return ordersRepo.save(newOrder);
    }
    
    public Orders updateOrderById(Long orderId, Orders updatedOrder) {
        Optional<Orders> orderOptional = ordersRepo.findById(orderId);
        if (orderOptional.isPresent()) {
            Orders order = orderOptional.get();

            // update hanya field yang tidak null, jika null gunakan value seperti
            // sebelumnya
            if (updatedOrder.getOrderCode() != null) {
                order.setOrderCode(updatedOrder.getOrderCode());
            }
            if (updatedOrder.getOrderDate() != null) {
                order.setOrderDate(updatedOrder.getOrderDate());
            }
            if (updatedOrder.getTotalPrice() != null) {
                order.setTotalPrice(updatedOrder.getTotalPrice());
            }
            if (updatedOrder.getCustomers() != null) {
                order.setCustomers(updatedOrder.getCustomers());
            }
            if (updatedOrder.getItems() != null) {
                order.setItems(updatedOrder.getItems());
            }
            if (updatedOrder.getQuantity() != null) {
                order.setQuantity(updatedOrder.getQuantity());
            }

            return ordersRepo.save(order);
        } else {
            return null;
        }
    }
    
}

