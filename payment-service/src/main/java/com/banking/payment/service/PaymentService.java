package com.banking.payment.service;

import com.banking.payment.entity.PaymentEntity;
import com.banking.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public PaymentService(PaymentRepository repository, KafkaTemplate<String, String> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public PaymentEntity processPayment(Long txId, String type, PaymentEntity payment) {
        logger.info("Processing payment request: txId={}, type={}, payment={}", txId, type, payment);
        
        // Use the provided payment entity and update specific fields
        payment.setTransactionId(txId);
        payment.setType(type);
        payment.setStatus(Math.random() > 0.1 ? "PROCESSED" : "FAILED"); // 90% success
        payment.setTimestamp(LocalDateTime.now()); // Set current timestamp

        PaymentEntity saved = repository.save(payment);
        logger.info("Saved payment entity: {}", saved);

        // Send JSON message to Kafka
        try {
            String jsonMessage = objectMapper.writeValueAsString(saved);
            kafkaTemplate.send("payment-callback", jsonMessage);
            logger.info("Sent Kafka message: {}", jsonMessage);
        } catch (Exception e) {
            logger.error("Failed to send Kafka message: {}", e.getMessage());
        }

        return saved;
    }
}