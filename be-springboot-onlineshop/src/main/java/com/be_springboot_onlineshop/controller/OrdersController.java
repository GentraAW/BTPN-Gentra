package com.be_springboot_onlineshop.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.be_springboot_onlineshop.model.Customers;
import com.be_springboot_onlineshop.model.Items;
import com.be_springboot_onlineshop.model.Orders;
import com.be_springboot_onlineshop.repository.CustomersRepo;
import com.be_springboot_onlineshop.repository.ItemsRepo;
import com.be_springboot_onlineshop.repository.OrdersRepo;
import com.be_springboot_onlineshop.service.OrdersService;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @GetMapping
    public ResponseEntity<?> getAllOrders(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "orderDate") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(required = false) String customerName) {

        // ubah size menjadi Integer.MAX_VALUE untuk menampilkan semua data pada satu halaman.
        if (size == 5 && !isSizeProvidedByClient()) {
            size = Integer.MAX_VALUE;
        }

        Page<Orders> ordersPage = ordersService.getAllOrders(page, size, sortBy, direction, customerName);

        if (ordersPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data sudah tidak tersedia");
        }

        return ResponseEntity.ok(ordersPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id) {
        Optional<Orders> order = ordersService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Orders> createOrder(
            @RequestParam(name = "customerId") Long customerId,
            @RequestParam(name = "itemId") Long itemId,
            @RequestParam(name = "quantity") Integer quantity) {
        
        Orders newOrder = new Orders();
        // newOrder.setOrderDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
        newOrder.setOrderDate( new Date());
        newOrder.setQuantity(quantity);

        Customers customer = new Customers();
        customer.setCustomerId(customerId);
        newOrder.setCustomers(customer);

        Items item = new Items();
        item.setItemId(itemId);
        newOrder.setItems(item);

        Orders order = ordersService.createOrder(newOrder);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orders> updateOrderById(
            @PathVariable Long id,
            @RequestParam(name = "orderCode", required = false) String orderCode,
            @RequestParam(name = "quantity", required = false) Integer quantity) {

        Orders updatedOrder = new Orders();
        updatedOrder.setOrderId(id);
        updatedOrder.setOrderCode(orderCode);
        updatedOrder.setQuantity(quantity);

        Orders order = ordersService.updateOrderById(id, updatedOrder);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable Long id) {
        try {
            ordersService.deleteOrderById(id);
            return ResponseEntity.ok("Data berhasil dihapus.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private boolean isSizeProvidedByClient() {
        return !((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .getRequest().getParameterMap().containsKey("size");
    }
}
