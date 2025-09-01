package com.banking.audit.service;

import com.banking.audit.entity.AuditLogEntity;
import com.banking.audit.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final AuditRepository repository;

    @Autowired
    public AuditService(AuditRepository repository) {
        this.repository = repository;
    }

    public void log(String eventType, String details, String who, String what, String amount, String toWhom) {
        AuditLogEntity log = new AuditLogEntity();
        log.setEventType(eventType);
        log.setDetails(details);
        log.setWho(who);
        log.setWhat(what);
        log.setAmount(amount);
        log.setToWhom(toWhom);
        repository.save(log);
    }

    public List<AuditLogEntity> getAll() {
        return repository.findAll();
    }
}