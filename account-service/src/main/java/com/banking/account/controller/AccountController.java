package com.banking.account.controller;

import com.banking.account.entity.AccountEntity;
import com.banking.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public AccountEntity create(@RequestBody AccountEntity account) {
        return service.create(account);
    }

    @PostMapping("/{id}/debit")
    public void debit(@PathVariable Long id, @RequestBody BigDecimal amount) {
        service.debit(id, amount);
    }

    @PostMapping("/{id}/credit")
    public void credit(@PathVariable Long id, @RequestBody BigDecimal amount) {
        service.credit(id, amount);
    }

    @PostMapping("/{id}/hold")
    public void hold(@PathVariable Long id, @RequestBody String reason) {
        service.hold(id, reason);
    }

    @GetMapping("/{id}")
    public AccountEntity get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<AccountEntity> getByCustomer(@PathVariable Long customerId) {
        return service.getByCustomerId(customerId);
    }
}