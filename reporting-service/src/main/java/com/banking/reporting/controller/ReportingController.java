package com.banking.reporting.controller;

import com.banking.reporting.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportingController {

    private final ReportingService service;

    @Autowired
    public ReportingController(ReportingService service) {
        this.service = service;
    }

    @GetMapping("/transactions/{accountId}")
    public String transactionReport(@PathVariable Long accountId) {
        return service.generateTransactionReport(accountId);
    }

    @GetMapping("/audits")
    public String auditReport() {
        return service.generateAuditReport();
    }
}