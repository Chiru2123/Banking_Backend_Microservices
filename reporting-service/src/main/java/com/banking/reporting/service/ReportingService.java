package com.banking.reporting.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReportingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateTransactionReport(Long accountId) {
        String history = restTemplate.getForObject("http://localhost:8080/transactions/history/" + accountId, String.class);
        return "Transaction Report for account " + accountId + ": " + history;
    }

    public String generateAuditReport() {
        String audits = restTemplate.getForObject("http://localhost:8080/audits", String.class);
        return "Audit Report: " + audits;
    }
}