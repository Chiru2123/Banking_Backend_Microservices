package com.banking.transaction.controller;

import com.banking.transaction.entity.TransactionEntity;
import com.banking.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/internal")
    public TransactionEntity internalTransfer(@RequestBody TransactionEntity tx) {
        return service.initiateInternal(tx);
    }

    @PostMapping("/external/{type}")
    public TransactionEntity externalTransfer(@PathVariable String type, @RequestBody TransactionEntity tx) {
        return service.initiateExternal(tx, type);
    }

    @GetMapping("/history/{accountId}")
    public List<TransactionEntity> history(@PathVariable Long accountId) {
        return service.getHistory(accountId);
    }

    @GetMapping("/paged-history/{accountId}")
    public List<TransactionEntity> pagedHistory(@PathVariable Long accountId, @RequestParam int page, @RequestParam int size) {
        return service.getPagedHistory(accountId, page, size);
    }
}