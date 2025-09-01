package com.banking.notification.listener;

import com.banking.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final NotificationService service;

    @Autowired
    public NotificationListener(NotificationService service) {
        this.service = service;
    }

    @KafkaListener(topics = {"transaction-events", "kyc-events", "otp-events", "notification-events", "account-events"}, groupId = "notification-group")
    public void listen(String message) {
        String[] parts = message.split(":");
        String type = parts[0];
        String details = parts[1];
        String phone = parts.length > 2 ? parts[2] : "";
        String email = parts.length > 3 ? parts[3] : "";
        String msg = type + ": " + details;
        if (!phone.isEmpty()) service.sendSms(phone, msg);
        if (!email.isEmpty()) service.sendEmail(email, msg);
    }
}