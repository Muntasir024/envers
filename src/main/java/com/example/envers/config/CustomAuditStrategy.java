package com.example.envers.config;

import org.hibernate.Session;
import org.hibernate.envers.RevisionListener;
import org.hibernate.envers.configuration.internal.AuditEntitiesConfiguration;
import org.hibernate.envers.strategy.internal.DefaultAuditStrategy;

import java.io.Serializable;

public class CustomAuditStrategy extends DefaultAuditStrategy {

    @Override
    public void perform(
            Session session, String entityName, AuditEntitiesConfiguration auditEntitiesConfiguration, Serializable id, Object revision, Object entity) {
        // Ensure the session is tied to the audit database
        super.perform(session, entityName, auditEntitiesConfiguration, id, revision, entity);
    }
}
