package com.example.envers.repository;

import com.example.envers.model.*;
import com.example.envers.utill.AuditContext;
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

    public List<ProductDifference> getProductDifferences(Long productId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<ProductDifference> differences = new ArrayList<>();

        List<Number> revisions = auditReader.getRevisions(Product.class, productId);

        for (int i = 1; i < revisions.size(); i++) {
            Product previousVersion = auditReader.find(Product.class, productId, revisions.get(i - 1));
            Product currentVersion = auditReader.find(Product.class, productId, revisions.get(i));

            differences.add(calculateDifference(previousVersion, currentVersion, revisions.get(i)));
        }
        return differences;
    }

    private ProductDifference calculateDifference(Product previous, Product current, Number revisionId) {
        ProductDifference difference = new ProductDifference();
        difference.setRevisionId(revisionId);
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        Revision revision = auditReader.findRevision(Revision.class, revisionId);
        difference.setRevisionId(revisionId);

        difference.setUsername(revision.getUsername());
        difference.setTimestamp(revision.getRevisionDate());

        if (!previous.getName().equals(current.getName())) {
            difference.addDifference("name", previous.getName(), current.getName());
        }
        if (!previous.getSku().equals(current.getSku())) {
            difference.addDifference("sku", previous.getSku(), current.getSku());
        }
        if (!previous.getQuantity().equals(current.getQuantity())) {
            difference.addDifference("quantity", previous.getQuantity(), current.getQuantity());
        }
        if (!previous.getPrice().equals(current.getPrice())) {
            difference.addDifference("price", previous.getPrice(), current.getPrice());
        }

        return difference;
    }
}
