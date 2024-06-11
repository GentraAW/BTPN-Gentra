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
        // Mengambil data item berdasarkan item_id dari newOrder
        Items item = itemsRepo.findById(newOrder.getItems().getItemId()).orElse(null);

        Customers customer = customerRepo.findById(newOrder.getCustomers().getCustomerId()).orElse(null);

        // Jika item tidak ditemukan, kembalikan null
        if (item == null) {
            throw new IllegalArgumentException("Item dengan ID " + newOrder.getItems().getItemId() + " tidak ditemukan");
        }

        Integer totalPrice = item.getPrice() * newOrder.getQuantity();

        Integer updatedStock = item.getStock() - newOrder.getQuantity();
        item.setStock(updatedStock);
        item.setLastReStock(new Date()); // Update lastReStock ke tanggal saat ini

        // Update total price dan stock item pada newOrder
        newOrder.setTotalPrice(totalPrice);
        newOrder.getItems().setStock(updatedStock); // Update stock pada item di newOrder

        if (customer == null) {
            throw new IllegalArgumentException("Customers dengan ID " + newOrder.getCustomers().getCustomerId() + " tidak ditemukan");
        }
        customer.setLastOrderDate(new Date());

        // Simpan order dan item yang telah diupdate
        ordersRepo.save(newOrder);
        itemsRepo.save(item);

        return newOrder;
    }
    
    public Orders updateOrderById(Long orderId, Orders updatedOrder) {
        Optional<Orders> orderOptional = ordersRepo.findById(orderId);
        if (orderOptional.isPresent()) {
            Orders order = orderOptional.get();

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
    
    public void deleteOrderById(Long orderId) {
        if (ordersRepo.existsById(orderId)) {
            ordersRepo.deleteById(orderId);
        } else {
            throw new IllegalArgumentException("ID dengan nilai " + orderId + " tidak ditemukan");
        }
    }
}

