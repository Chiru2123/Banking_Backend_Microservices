package com.banking.transaction.service;


import com.banking.transaction.entity.TransactionEntity;
import com.banking.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final KafkaTemplate<String, SagaEvent> kafkaTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public TransactionService(TransactionRepository repository, KafkaTemplate<String, SagaEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public TransactionEntity initiateInternal(TransactionEntity tx) {
        if (repository.findByIdempotencyKey(tx.getIdempotencyKey()) != null) {
            throw new RuntimeException("Duplicate transaction");
        }
        tx.setIdempotencyKey(UUID.randomUUID().toString());
        tx.setStatus("INITIATED");
        TransactionEntity saved = repository.save(tx);
        kafkaTemplate.send("transaction-saga", new SagaEvent("DEBIT", saved.getId()));
        return saved;
    }

    public TransactionEntity initiateExternal(TransactionEntity tx, String externalType) {
        tx.setType(externalType);
        tx.setIdempotencyKey(UUID.randomUUID().toString());
        tx.setStatus("INITIATED");
        TransactionEntity saved = repository.save(tx);
        restTemplate.exchange("http://localhost:8080/payments/external?txId=" + saved.getId() + "&type=" + externalType, HttpMethod.POST, new HttpEntity<>(saved), Void.class);
        return saved;
    }

    @KafkaListener(topics = "transaction-saga", groupId = "transaction-group")
    public void handleSaga(SagaEvent event) {
        TransactionEntity tx = repository.findById(event.getTxId()).orElseThrow();
        if ("DEBIT".equals(event.getStep())) {
            try {
                restTemplate.postForEntity("http://localhost:8080/accounts/" + tx.getFromAccount() + "/debit", tx.getAmount(), Void.class);
                kafkaTemplate.send("transaction-saga", new SagaEvent("CREDIT", tx.getId()));
            } catch (Exception e) {
                kafkaTemplate.send("transaction-saga", new SagaEvent("COMPENSATE", tx.getId()));
            }
        } else if ("CREDIT".equals(event.getStep())) {
            restTemplate.postForEntity("http://localhost:8080/accounts/" + tx.getToAccount() + "/credit", tx.getAmount(), Void.class);
            tx.setStatus("COMPLETED");
            repository.save(tx);
            kafkaTemplate.send("transaction-events", new SagaEvent("COMPLETED", tx.getId()));
        } else if ("COMPENSATE".equals(event.getStep())) {
            restTemplate.postForEntity("http://localhost:8080/accounts/" + tx.getFromAccount() + "/credit", tx.getAmount(), Void.class);
            tx.setStatus("FAILED");
            repository.save(tx);
        }
    }

    @KafkaListener(topics = "payment-callback", groupId = "transaction-group")
    public void handlePaymentCallback(String message) {
        String[] parts = message.split(":");
        Long txId = Long.parseLong(parts[1]);
        TransactionEntity tx = repository.findById(txId).orElseThrow();
        tx.setStatus(parts[0]);
        repository.save(tx);
        kafkaTemplate.send("transaction-events", new SagaEvent(parts[0], txId));
    }

    public List<TransactionEntity> getHistory(Long accountId) {
        return repository.findByFromAccountOrToAccount(accountId, accountId);
    }

    public List<TransactionEntity> getPagedHistory(Long accountId, int page, int size) {
        return repository.findByFromAccountOrToAccount(accountId, accountId, Pageable.ofSize(size).withPage(page)).getContent();
    }
}

