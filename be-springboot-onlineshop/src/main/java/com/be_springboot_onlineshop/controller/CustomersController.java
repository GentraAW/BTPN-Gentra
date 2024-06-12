package com.be_springboot_onlineshop.controller;

import com.be_springboot_onlineshop.model.Customers;
import com.be_springboot_onlineshop.service.CustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomersController {

    @Autowired
    private CustomersService customersService;

    @GetMapping
    public ResponseEntity<?> getAllActiveCustomers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "customerName") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(required = false) String customerName) {

        // ubah size menjadi Integer.MAX_VALUE untuk menampilkan semua data pada satu halaman.
        if (size == 5 && !isSizeProvidedByClient()) {
            size = Integer.MAX_VALUE;
        }

        Page<Customers> activeCustomers = customersService.getAllActiveCustomers(page, size, sortBy, direction, customerName);

        if (activeCustomers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("customer aktif tidak ditemukan");
        }

        return ResponseEntity.ok(activeCustomers);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Customers> getCustomerById(@PathVariable Long id) {
        Optional<Customers> customer = customersService.getCustomerById(id);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(
            @RequestParam("customerName") String customerName,
            @RequestParam("customerPhone") String customerPhone,
            @RequestParam("customerAddress") String customerAddress,
            @RequestParam(value = "isActive", defaultValue = "true") Boolean isActive,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date lastOrderDate,
            @RequestParam(required = false) MultipartFile file) {
        try {
            Customers newCustomer = new Customers();
            newCustomer.setCustomerName(customerName);
            newCustomer.setCustomerPhone(customerPhone);
            newCustomer.setCustomerAddress(customerAddress);
            newCustomer.setIsActive(isActive);
            newCustomer.setLastOrderDate(lastOrderDate);

            Customers createdCustomer = customersService.createCustomer(newCustomer, file);
            return ResponseEntity.ok(createdCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customers> updateCustomerById(
            @PathVariable Long id,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String customerAddress,
            @RequestParam(required = false) String customerCode,
            @RequestParam(required = false) String customerPhone,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date lastOrderDate,
            @RequestParam(required = false) MultipartFile file) {

        Customers updatedCustomer = new Customers();
        updatedCustomer.setCustomerName(customerName);
        updatedCustomer.setCustomerAddress(customerAddress);
        updatedCustomer.setCustomerCode(customerCode);
        updatedCustomer.setCustomerPhone(customerPhone);
        updatedCustomer.setIsActive(isActive);
        updatedCustomer.setLastOrderDate(lastOrderDate);

        try {
            Customers customer = customersService.updateCustomerById(id, updatedCustomer, file);
            return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

    private boolean isSizeProvidedByClient() {
        return !((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .getRequest().getParameterMap().containsKey("size");
    }
}
