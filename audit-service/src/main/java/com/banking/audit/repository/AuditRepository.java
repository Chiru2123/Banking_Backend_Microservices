package com.banking.audit.repository;

import com.banking.audit.entity.AuditLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditRepository extends MongoRepository<AuditLogEntity, String> {}