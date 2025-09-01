package com.banking.audit.controller;

import com.banking.audit.entity.AuditLogEntity;
import com.banking.audit.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audits")
public class AuditController {

    private final AuditService service;

    @Autowired
    public AuditController(AuditService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuditLogEntity> getAll() {
        return service.getAll();
    }
}