package com.be_springboot_onlineshop.service;
 
import java.util.Optional;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.be_springboot_onlineshop.model.Customers;
import com.be_springboot_onlineshop.model.Items;
import com.be_springboot_onlineshop.model.Orders;
import com.be_springboot_onlineshop.repository.CustomersRepo;
import com.be_springboot_onlineshop.repository.ItemsRepo;
import com.be_springboot_onlineshop.repository.OrdersRepo;

@Service
 public class OrdersService {
     
    @Autowired
    private OrdersRepo ordersRepo;

    @Autowired
    private ItemsRepo itemsRepo;

    @Autowired
    private CustomersRepo customerRepo;
    
    public Page<Orders> getAllOrders(int page, int size, String sortBy, String direction, String customerName) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Orders> ordersPage;
        if (customerName == null || customerName.isEmpty()) {
            ordersPage = ordersRepo.findAll(pageable);
        } else {
            ordersPage = ordersRepo.findByCustomersCustomerNameContainingIgnoreCase(customerName, pageable);
        }

        return ordersPage;
    }
    
    public Optional<Orders> getOrderById(Long orderId) {
        return ordersRepo.findByOrderId(orderId);
    }

    public Orders createOrder(Orders newOrder) {
        Items item = itemsRepo.findById(newOrder.getItems().getItemId()).orElse(null);
        Customers customer = customerRepo.findById(newOrder.getCustomers().getCustomerId()).orElse(null);

        if (item == null) {
            throw new IllegalArgumentException("Item dengan ID " + newOrder.getItems().getItemId() + " tidak ditemukan");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customers dengan ID " + newOrder.getCustomers().getCustomerId() + " tidak ditemukan");
        }

        if (!item.getIsAvailable()) {
            throw new IllegalArgumentException("Item dengan ID " + newOrder.getItems().getItemId() + " tidak tersedia");
        }
        if (!customer.getIsActive()) {
            throw new IllegalArgumentException("Customer dengan ID " + newOrder.getCustomers().getCustomerId() + " tidak aktif");
        }

        Integer totalPrice = item.getPrice() * newOrder.getQuantity();
        Integer updatedStock = item.getStock() - newOrder.getQuantity();
        item.setStock(updatedStock);
        item.setLastReStock(new Date());

        newOrder.setTotalPrice(totalPrice);
        newOrder.getItems().setStock(updatedStock);

        customer.setLastOrderDate(new Date());

        itemsRepo.save(item);

        Orders savedOrder = ordersRepo.save(newOrder);

        String orderCode = "ORD-" + savedOrder.getCustomers().getCustomerId() + "-" + 
                        savedOrder.getItems().getItemId() + "-" + savedOrder.getOrderId();
        savedOrder.setOrderCode(orderCode);

        ordersRepo.save(savedOrder);

        return savedOrder;
    }
    
    public Orders updateOrderById(Long orderId, Orders updatedOrder) {
        Optional<Orders> orderOptional = ordersRepo.findById(orderId);
         if (!orderOptional.isPresent()) {
            throw new IllegalArgumentException("Order dengan ID " + orderId + " tidak ditemukan");
        }
        if (orderOptional.isPresent()) {
            Orders order = orderOptional.get();

            if (updatedOrder.getOrderCode() != null) {
                order.setOrderCode(updatedOrder.getOrderCode());
            }
            if (updatedOrder.getQuantity() != null && order.getItems() != null) {
                Items item = itemsRepo.findById(order.getItems().getItemId()).orElse(null);
                Customers customer = customerRepo.findById(order.getCustomers().getCustomerId()).orElse(null);
                if (item != null) {
                    Integer oldQuantity = order.getQuantity() != null ? order.getQuantity() : 0;
                    Integer updatedStock = item.getStock() + oldQuantity - updatedOrder.getQuantity();
                    order.setQuantity(updatedOrder.getQuantity());
                    Integer totalPrice = item.getPrice() * updatedOrder.getQuantity();
                    order.setTotalPrice(totalPrice);
                    item.setStock(updatedStock);
                    item.setLastReStock(new Date());
                    customer.setLastOrderDate(new Date());
                }
            }

            return ordersRepo.save(order);
        } else {
            return null;
        }
    }

    public void deleteOrderById(Long orderId) {
        Optional<Orders> orderOptional = ordersRepo.findById(orderId);
        if (orderOptional.isPresent()) {
            Orders order = orderOptional.get();
            Items item = order.getItems();
            if (item != null) {
                Integer oldQuantity = order.getQuantity() != null ? order.getQuantity() : 0;
                item.setStock(item.getStock() + oldQuantity);
                itemsRepo.save(item);
            }
            ordersRepo.deleteById(orderId);
        } else {
            throw new IllegalArgumentException("Order Id : " + orderId + " tidak ditemukan");
        }
    }
}

