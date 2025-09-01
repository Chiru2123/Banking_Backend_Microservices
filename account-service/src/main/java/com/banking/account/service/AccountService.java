package com.banking.account.service;

import com.banking.account.entity.AccountEntity;
import com.banking.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public AccountService(AccountRepository repository, KafkaTemplate<String, String> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public AccountEntity create(AccountEntity account) {
        account.setBalance(BigDecimal.ZERO);
        AccountEntity saved = repository.save(account);
        kafkaTemplate.send("account-events", "ACCOUNT_OPENED:" + saved.getId() + ":" + account.getType() + ":" + "" + ":" + "");
        return saved;
    }

    public void debit(Long id, BigDecimal amount) {
        AccountEntity account = repository.findById(id).orElseThrow();
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));
        repository.save(account);
        if (account.getBalance().compareTo(BigDecimal.valueOf(100)) < 0) {
            kafkaTemplate.send("notification-events", "LOW_BALANCE:" + "Low balance for account " + id + ":" + "" + ":" + "");
        }
    }

    public void credit(Long id, BigDecimal amount) {
        AccountEntity account = repository.findById(id).orElseThrow();
        account.setBalance(account.getBalance().add(amount));
        repository.save(account);
    }

    public void hold(Long id, String reason) {
        AccountEntity account = repository.findById(id).orElseThrow();
        account.setHoldStatus("PENDING_" + reason);
        repository.save(account);
    }

    public AccountEntity getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<AccountEntity> getByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId);
    }
}