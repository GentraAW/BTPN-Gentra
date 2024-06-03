package com.be_springboot_onlineshop.controller;

import com.be_springboot_onlineshop.model.Customers;
import com.be_springboot_onlineshop.service.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomersController {
    @Autowired
    private CustomersService customersService;

    @GetMapping
    public List<Customers> getAllActiveCustomers() {
        return customersService.getAllActiveCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customers> getCustomerById(@PathVariable UUID id) {
        Optional<Customers> customer = customersService.getCustomerById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customers> createCustomer(@RequestBody Customers newCustomer) {
        Customers customer = customersService.createCustomer(newCustomer);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customers> updateCustomerById(@PathVariable UUID id, @RequestBody Customers updatedCustomer) {
        Customers customer = customersService.updateCustomerById(id, updatedCustomer);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable UUID id) {
        try {
            customersService.deleteCustomerId(id);
            return ResponseEntity.ok("Data berhasil dihapus.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
