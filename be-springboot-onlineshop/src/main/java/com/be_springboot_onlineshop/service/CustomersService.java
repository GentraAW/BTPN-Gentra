package com.be_springboot_onlineshop.service;

import com.be_springboot_onlineshop.model.Customers;
import com.be_springboot_onlineshop.model.Items;
import com.be_springboot_onlineshop.repository.CustomersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomersService {
    @Autowired
    private CustomersRepo customersRepo;

    public List<Customers> getAllActiveCustomers() {
        return customersRepo.findByIsActive(true);
    }

    public Optional<Customers> getCustomerById(UUID customerId) {
        return customersRepo.findByCustomerIdAndIsActive(customerId, true);
    }

    public Customers createCustomer(Customers newCustomer) {
        return customersRepo.save(newCustomer);
    }

    public Customers updateCustomerById(UUID customerId, Customers updatedCustomer) {
        Optional<Customers> customerOptional = customersRepo.findById(customerId);
        if (customerOptional.isPresent()) {
            Customers customer = customerOptional.get();
            
            // update hanya field yang tidak null, jika null gunakan value seperti sebelumnya
            if (updatedCustomer.getCustomerName() != null) {
                customer.setCustomerName(updatedCustomer.getCustomerName());
            }
            if (updatedCustomer.getCustomerCode() != null) {
                customer.setCustomerCode(updatedCustomer.getCustomerCode());
            }
            if (updatedCustomer.getCustomerPhone() != null) {
                customer.setCustomerPhone(updatedCustomer.getCustomerPhone());
            }
            if (updatedCustomer.getIsActive() != null) {
                customer.setIsActive(updatedCustomer.getIsActive());
            }
            if (updatedCustomer.getLastOrderDate() != null) {
                customer.setLastOrderDate(updatedCustomer.getLastOrderDate());
            }
            if (updatedCustomer.getPic() != null) {
                customer.setPic(updatedCustomer.getPic());
            }

            return customersRepo.save(customer);
        } else {
            return null;
        }
    }

    public void deleteCustomerId(UUID customerId) {
        Optional<Customers> customerOptional = customersRepo.findById(customerId);
        if (customerOptional.isPresent()) {
            Customers customer = customerOptional.get();
            if (!customer.getIsActive()) {
                throw new IllegalArgumentException("Data dengan ID tersebut sudah dihapus.");
            }
            customer.setIsActive(false);
            customersRepo.save(customer);
        } else {
            throw new IllegalArgumentException("ID tidak di temukan.");
        }
    }
}
