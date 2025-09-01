package com.banking.audit.listener;

import com.banking.audit.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuditListener {

    private final AuditService service;

    @Autowired
    public AuditListener(AuditService service) {
        this.service = service;
    }

    @KafkaListener(topics = {"login-events", "transaction-events", "kyc-events", "account-events"}, groupId = "audit-group")
    public void listen(String message) {
        String[] parts = message.split(":");
        String eventType = parts[0];  // Simplified parsing
        String details = message;
        String who = parts.length > 2 ? parts[2] : "";
        String what = parts.length > 3 ? parts[3] : "";
        String amount = parts.length > 4 ? parts[4] : "";
        String toWhom = parts.length > 5 ? parts[5] : "";
        service.log(eventType, details, who, what, amount, toWhom);
    }
}