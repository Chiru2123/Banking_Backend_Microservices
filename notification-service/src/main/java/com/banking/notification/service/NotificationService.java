package com.banking.notification.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.banking.transaction.service.SagaEvent;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class NotificationService {
	
	private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public NotificationService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(SagaEvent event) {
        kafkaTemplate.send("notification-events", event.getTxId().toString(), event);
    }

    public void sendSms(String phone, String message) {
        System.out.println("SMS to " + phone + ": " + message);  // Mock
    }

    public String sendOtp(String phone, String message) {
        System.out.println("SMS to " + phone + ": " + message);  // Mock
        return "OTP sent";
    }

    public void sendEmail(String email, String message) {
        System.out.println("Email to " + email + ": " + message);  // Mock
    }
}