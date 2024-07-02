package com.be_springboot_onlineshop.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.be_springboot_onlineshop.model.Orders;
import com.be_springboot_onlineshop.repository.OrdersRepo;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportService {

    @Autowired
    private OrdersRepo ordersRepo;

    public ResponseEntity<byte[]> exportReport(String reportFormat) {
        try {
            List<Orders> orders = ordersRepo.findAll();

            // mengompilasi file JRXML
            File file = ResourceUtils.getFile("classpath:Order.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            //buat data source
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orders);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Gen");

            // isi laporan dengan data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // eksport laporan ke byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (reportFormat.equalsIgnoreCase("pdf")) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            } else {
                return ResponseEntity.badRequest().body(null);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "Order.pdf");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(500).body(null);
        } catch (JRException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
