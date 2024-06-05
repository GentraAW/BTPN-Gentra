package com.be_springboot_onlineshop.controller;

import com.be_springboot_onlineshop.model.Customers;
import com.be_springboot_onlineshop.service.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Customers> getCustomerById(@PathVariable Long id) {
        Optional<Customers> customer = customersService.getCustomerById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // @PostMapping
    // public ResponseEntity<Customers> createCustomer(@RequestBody Customers newCustomer) {
    //     Customers customer = customersService.createCustomer(newCustomer);
    //     return ResponseEntity.ok(customer);
    // }

    @PostMapping
    public ResponseEntity<Customers> createCustomer(
            @RequestParam("customerName") String customerName,
            @RequestParam("customerCode") String customerCode,
            @RequestParam("customerPhone") String customerPhone,
            @RequestParam("isActive") Boolean isActive,
            @RequestParam("lastOrderDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date lastOrderDate,
            @RequestParam("file") MultipartFile file) {
        try {
            Customers newCustomer = new Customers();
            newCustomer.setCustomerName(customerName);
            newCustomer.setCustomerCode(customerCode);
            newCustomer.setCustomerPhone(customerPhone);
            newCustomer.setIsActive(isActive);
            newCustomer.setLastOrderDate(lastOrderDate);

            Customers createdCustomer = customersService.createCustomer(newCustomer, file);
            return ResponseEntity.ok(createdCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customers> updateCustomerById(
            @PathVariable Long id,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String customerCode,
            @RequestParam(required = false) String customerPhone,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date lastOrderDate,
            @RequestParam(required = false) MultipartFile file) {
        
        Customers updatedCustomer = new Customers();
        updatedCustomer.setCustomerName(customerName);
        updatedCustomer.setCustomerCode(customerCode);
        updatedCustomer.setCustomerPhone(customerPhone);
        updatedCustomer.setIsActive(isActive);
        updatedCustomer.setLastOrderDate(lastOrderDate);

        try {
            Customers customer = customersService.updateCustomerById(id, updatedCustomer, file);
            if (customer != null) {
                return ResponseEntity.ok(customer);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable Long id) {
        try {
            customersService.deleteCustomerId(id);
            return ResponseEntity.ok("Data berhasil dihapus.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
