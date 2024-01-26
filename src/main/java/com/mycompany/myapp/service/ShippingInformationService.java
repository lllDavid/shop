package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ShippingInformation;
import com.mycompany.myapp.repository.ShippingInformationRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ShippingInformation}.
 */
@Service
@Transactional
public class ShippingInformationService {

    private final Logger log = LoggerFactory.getLogger(ShippingInformationService.class);

    private final ShippingInformationRepository shippingInformationRepository;

    public ShippingInformationService(ShippingInformationRepository shippingInformationRepository) {
        this.shippingInformationRepository = shippingInformationRepository;
    }

    /**
     * Save a shippingInformation.
     *
     * @param shippingInformation the entity to save.
     * @return the persisted entity.
     */
    public ShippingInformation save(ShippingInformation shippingInformation) {
        log.debug("Request to save ShippingInformation : {}", shippingInformation);
        return shippingInformationRepository.save(shippingInformation);
    }

    /**
     * Update a shippingInformation.
     *
     * @param shippingInformation the entity to save.
     * @return the persisted entity.
     */
    public ShippingInformation update(ShippingInformation shippingInformation) {
        log.debug("Request to update ShippingInformation : {}", shippingInformation);
        return shippingInformationRepository.save(shippingInformation);
    }

    /**
     * Partially update a shippingInformation.
     *
     * @param shippingInformation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShippingInformation> partialUpdate(ShippingInformation shippingInformation) {
        log.debug("Request to partially update ShippingInformation : {}", shippingInformation);

        return shippingInformationRepository
            .findById(shippingInformation.getId())
            .map(existingShippingInformation -> {
                if (shippingInformation.getDeliveryAddress() != null) {
                    existingShippingInformation.setDeliveryAddress(shippingInformation.getDeliveryAddress());
                }
                if (shippingInformation.getShippingStatus() != null) {
                    existingShippingInformation.setShippingStatus(shippingInformation.getShippingStatus());
                }

                return existingShippingInformation;
            })
            .map(shippingInformationRepository::save);
    }

    /**
     * Get all the shippingInformations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShippingInformation> findAll() {
        log.debug("Request to get all ShippingInformations");
        return shippingInformationRepository.findAll();
    }

    /**
     *  Get all the shippingInformations where Order is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShippingInformation> findAllWhereOrderIsNull() {
        log.debug("Request to get all shippingInformations where Order is null");
        return StreamSupport
            .stream(shippingInformationRepository.findAll().spliterator(), false)
            .filter(shippingInformation -> shippingInformation.getOrder() == null)
            .toList();
    }

    /**
     * Get one shippingInformation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShippingInformation> findOne(Long id) {
        log.debug("Request to get ShippingInformation : {}", id);
        return shippingInformationRepository.findById(id);
    }

    /**
     * Delete the shippingInformation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShippingInformation : {}", id);
        shippingInformationRepository.deleteById(id);
    }
}
