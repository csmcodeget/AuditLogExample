package org.example.config;

package com.example.audit.config;

import com.example.audit.AuditService;
import com.example.audit.AuditService.AuditLogSaver;
import com.example.audit.mapper.AuditLogMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AuditConfig {

    @Bean
    public AuditService auditService(AuditLogSaver auditLogSaver) {
        return new AuditService(auditLogSaver);
    }

    @Bean
    public AuditLogSaver auditLogSaver(AuditLogMapper auditLogMapper) {
        return new AsyncAuditLogSaver(auditLogMapper);
    }

    /**
     * 异步日志保存器
     */
    public static class AsyncAuditLogSaver implements AuditLogSaver {

        private final AuditLogMapper auditLogMapper;

        public AsyncAuditLogSaver(AuditLogMapper auditLogMapper) {
            this.auditLogMapper = auditLogMapper;
        }

        @Async
        @Override
        public void save(AuditLog auditLog) {
            auditLogMapper.insert(auditLog);
        }
    }
}