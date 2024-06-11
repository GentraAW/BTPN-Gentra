package com.be_springboot_onlineshop.service;

import com.be_springboot_onlineshop.model.Customers;
import com.be_springboot_onlineshop.repository.CustomersRepo;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Date;

@Service
public class CustomersService {
    @Autowired
    private CustomersRepo customersRepo;

    @Autowired
    private MinioClient minioClient;

    @Value("${application.minio.bucketName}")
    private String bucketName;

    public Page<Customers> getAllActiveCustomers(int page, int size, String sortBy, String direction, String customerName) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Customers> activeCustomersPage;
        if (customerName == null || customerName.isEmpty()) {
            activeCustomersPage = customersRepo.findByIsActive(true, pageable);
        } else {
            activeCustomersPage = customersRepo.findByCustomerNameContainingIgnoreCaseAndIsActive(customerName, true, pageable);
        }
        
        return activeCustomersPage.map(this::setPicUrlIfNeeded);
    }

    public Optional<Customers> getCustomerById(Long customerId) {
        Optional<Customers> customerOptional = customersRepo.findByCustomerIdAndIsActive(customerId, true);
        return customerOptional.map(this::setPicUrlIfNeeded);
    }

    private Customers setPicUrlIfNeeded(Customers customer) {
        String pic = customer.getPic();
        if (pic != null) {
            String picUrl = "http://localhost:9000/" + bucketName + "/" + pic;
            customer.setPic(picUrl);
        }
        return customer;
    }
    
     public Customers createCustomer(Customers newCustomer, MultipartFile file) throws Exception {
        long timestamp = new Date().getTime();

        String customerPhone = newCustomer.getCustomerPhone();
        newCustomer.setCustomerCode("CUST-" + customerPhone);

        if (newCustomer.getIsActive() == null) {
            newCustomer.setIsActive(true);
        }

        if (newCustomer.getLastOrderDate() == null) {
            newCustomer.setLastOrderDate(new Date());
        }

        if (customersRepo.existsByCustomerPhone(customerPhone)) {
            throw new Exception("Nomor telepon sudah tersedia");
        }

        if (file != null) {
            String originalFilename = file.getOriginalFilename();
            String[] fileNameParts = originalFilename.split("\\.");
            String fileName = fileNameParts[0] + "." + timestamp + "." + fileNameParts[1];

            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(inputStream, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
                );
            }

            newCustomer.setPic(fileName);
        }

        // Save the new customer to the database
        return customersRepo.save(newCustomer);
    }

    public Customers updateCustomerById(Long customerId, Customers updatedCustomer, MultipartFile file) throws Exception {
        Optional<Customers> customerOptional = customersRepo.findById(customerId);
        if (customerOptional.isPresent()) {
            Customers customer = customerOptional.get();

            // Update hanya field yang tidak null
            if (updatedCustomer.getCustomerName() != null) {
                customer.setCustomerName(updatedCustomer.getCustomerName());
            }
            if(updatedCustomer.getCustomerAddress() != null) {
                customer.setCustomerAddress(updatedCustomer.getCustomerAddress());
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
            
            // Hapus file lama di MinIO jika ada file baru yang diunggah
            if (file != null && customer.getPic() != null) {
                try {
                    minioClient.removeObject(
                        RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(customer.getPic())
                            .build()
                    );
                } catch (Exception e) {
                    throw new RuntimeException("Error deleting old file from MinIO", e);
                }
            }

            // Jika ada file baru, unggah ke MinIO
            if (file != null) {
                long timestamp = new Date().getTime();
                String originalFilename = file.getOriginalFilename();
                String[] fileNameParts = originalFilename.split("\\.");
                String fileName = fileNameParts[0] + "." + timestamp + "." + fileNameParts[1];

                try (InputStream inputStream = file.getInputStream()) {
                    minioClient.putObject(
                        PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
                    );
                } catch (IOException e) {
                    throw new RuntimeException("Error uploading new file to MinIO", e);
                }

                customer.setPic(fileName);
            }

            return customersRepo.save(customer);
        } else {
            return null;
        }
    }

    public void deleteCustomerId(Long customerId) {
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
