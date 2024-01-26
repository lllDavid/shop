package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ShippingInformation;
import com.mycompany.myapp.repository.ShippingInformationRepository;
import com.mycompany.myapp.service.criteria.ShippingInformationCriteria;
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
 * Service for executing complex queries for {@link ShippingInformation} entities in the database.
 * The main input is a {@link ShippingInformationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShippingInformation} or a {@link Page} of {@link ShippingInformation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShippingInformationQueryService extends QueryService<ShippingInformation> {

    private final Logger log = LoggerFactory.getLogger(ShippingInformationQueryService.class);

    private final ShippingInformationRepository shippingInformationRepository;

    public ShippingInformationQueryService(ShippingInformationRepository shippingInformationRepository) {
        this.shippingInformationRepository = shippingInformationRepository;
    }

    /**
     * Return a {@link List} of {@link ShippingInformation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShippingInformation> findByCriteria(ShippingInformationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShippingInformation> specification = createSpecification(criteria);
        return shippingInformationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ShippingInformation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShippingInformation> findByCriteria(ShippingInformationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShippingInformation> specification = createSpecification(criteria);
        return shippingInformationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShippingInformationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShippingInformation> specification = createSpecification(criteria);
        return shippingInformationRepository.count(specification);
    }

    /**
     * Function to convert {@link ShippingInformationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShippingInformation> createSpecification(ShippingInformationCriteria criteria) {
        Specification<ShippingInformation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShippingInformation_.id));
            }
            if (criteria.getDeliveryAddress() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDeliveryAddress(), ShippingInformation_.deliveryAddress));
            }
            if (criteria.getShippingStatus() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getShippingStatus(), ShippingInformation_.shippingStatus));
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(ShippingInformation_.customer, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
            if (criteria.getOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderId(),
                            root -> root.join(ShippingInformation_.order, JoinType.LEFT).get(Order_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
