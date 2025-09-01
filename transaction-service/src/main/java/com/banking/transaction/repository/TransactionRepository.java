package com.banking.transaction.repository;

import com.banking.transaction.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByFromAccountOrToAccount(Long from, Long to);
    Page<TransactionEntity> findByFromAccountOrToAccount(Long from, Long to, Pageable pageable);
    TransactionEntity findByIdempotencyKey(String key);
}