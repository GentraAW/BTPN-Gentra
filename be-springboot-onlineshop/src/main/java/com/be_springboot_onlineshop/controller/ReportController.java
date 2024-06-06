package com.be_springboot_onlineshop.controller;

import com.be_springboot_onlineshop.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/orders")
    public ResponseEntity<byte[]> generateOrderReport(@RequestParam String format) {
        return reportService.exportReport(format);
    }
}
