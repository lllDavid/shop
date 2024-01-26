package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PaymentMethod;
import com.mycompany.myapp.repository.PaymentMethodRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.PaymentMethod}.
 */
@Service
@Transactional
public class PaymentMethodService {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodService.class);

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    /**
     * Save a paymentMethod.
     *
     * @param paymentMethod the entity to save.
     * @return the persisted entity.
     */
    public PaymentMethod save(PaymentMethod paymentMethod) {
        log.debug("Request to save PaymentMethod : {}", paymentMethod);
        return paymentMethodRepository.save(paymentMethod);
    }

    /**
     * Update a paymentMethod.
     *
     * @param paymentMethod the entity to save.
     * @return the persisted entity.
     */
    public PaymentMethod update(PaymentMethod paymentMethod) {
        log.debug("Request to update PaymentMethod : {}", paymentMethod);
        return paymentMethodRepository.save(paymentMethod);
    }

    /**
     * Partially update a paymentMethod.
     *
     * @param paymentMethod the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentMethod> partialUpdate(PaymentMethod paymentMethod) {
        log.debug("Request to partially update PaymentMethod : {}", paymentMethod);

        return paymentMethodRepository
            .findById(paymentMethod.getId())
            .map(existingPaymentMethod -> {
                if (paymentMethod.getName() != null) {
                    existingPaymentMethod.setName(paymentMethod.getName());
                }
                if (paymentMethod.getDescription() != null) {
                    existingPaymentMethod.setDescription(paymentMethod.getDescription());
                }

                return existingPaymentMethod;
            })
            .map(paymentMethodRepository::save);
    }

    /**
     * Get all the paymentMethods.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentMethod> findAll() {
        log.debug("Request to get all PaymentMethods");
        return paymentMethodRepository.findAll();
    }

    /**
     * Get one paymentMethod by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentMethod> findOne(Long id) {
        log.debug("Request to get PaymentMethod : {}", id);
        return paymentMethodRepository.findById(id);
    }

    /**
     * Delete the paymentMethod by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentMethod : {}", id);
        paymentMethodRepository.deleteById(id);
    }
}
