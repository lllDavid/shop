package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.PaymentMethod;
import com.mycompany.myapp.repository.PaymentMethodRepository;
import com.mycompany.myapp.service.criteria.PaymentMethodCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PaymentMethod} entities in the database.
 * The main input is a {@link PaymentMethodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentMethod} or a {@link Page} of {@link PaymentMethod} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentMethodQueryService extends QueryService<PaymentMethod> {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodQueryService.class);

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodQueryService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentMethod} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentMethod> findByCriteria(PaymentMethodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentMethod> specification = createSpecification(criteria);
        return paymentMethodRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PaymentMethod} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentMethod> findByCriteria(PaymentMethodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentMethod> specification = createSpecification(criteria);
        return paymentMethodRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentMethodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentMethod> specification = createSpecification(criteria);
        return paymentMethodRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentMethodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentMethod> createSpecification(PaymentMethodCriteria criteria) {
        Specification<PaymentMethod> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentMethod_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PaymentMethod_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PaymentMethod_.description));
            }
            if (criteria.getOrdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrdersId(), root -> root.join(PaymentMethod_.orders, JoinType.LEFT).get(Order_.id))
                    );
            }
        }
        return specification;
    }
}
