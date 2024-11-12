package com.example.envers.repository;

import com.example.envers.model.EntityRev;
import com.example.envers.model.Product;
import com.example.envers.model.ProductAudit;
import com.example.envers.model.Revision;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class GenericRevisionRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    public List<EntityRev<T>> revisionList(Long id, Class<T> type) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        List<Number> revisionIds = auditReader.getRevisions(type, id);
        Map<Number, Revision> revisions = auditReader.findRevisions(Revision.class, new HashSet<>(revisionIds));

        List<EntityRev<T>> result = new ArrayList<>();
        for (Number revisionId : revisionIds) {
            T entityState = auditReader.find(type, id, revisionId);
            Object revisionData = revisions.get(revisionId);
            Revision revision = new Revision();
            if (revisionData instanceof Revision) {
                revision = (Revision) revisionData;
            }

            if (entityState != null && revisionData != null) {
                result.add(new EntityRev<>(revision, entityState));
            }
        }

        return result;
    }

    public List<Object[]> getAllProductAudits() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        // Query all revisions for the Product entity
        List<Object[]> auditData = auditReader.createQuery()
                .forRevisionsOfEntity(Product.class, false, true)
                .addOrder(AuditEntity.revisionNumber().asc())
                .getResultList();

        return auditData;
    }
}
