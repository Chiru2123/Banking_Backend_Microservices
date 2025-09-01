package com.banking.payment.controller;

import com.banking.payment.entity.PaymentEntity;
import com.banking.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/external")
    public Mono<ResponseEntity<PaymentEntity>> processExternalPayment(
            @RequestParam Long txId,
            @RequestParam String type,
            @RequestBody PaymentEntity payment) {
        logger.info("Received payment request: txId={}, type={}, payment={}", txId, type, payment);
        return Mono.just(paymentService.processPayment(txId, type, payment))
                .map(ResponseEntity::ok);
    }
}