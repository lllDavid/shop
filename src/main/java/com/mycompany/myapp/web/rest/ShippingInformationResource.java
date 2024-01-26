package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ShippingInformation;
import com.mycompany.myapp.repository.ShippingInformationRepository;
import com.mycompany.myapp.service.ShippingInformationQueryService;
import com.mycompany.myapp.service.ShippingInformationService;
import com.mycompany.myapp.service.criteria.ShippingInformationCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ShippingInformation}.
 */
@RestController
@RequestMapping("/api/shipping-informations")
public class ShippingInformationResource {

    private final Logger log = LoggerFactory.getLogger(ShippingInformationResource.class);

    private static final String ENTITY_NAME = "shippingInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShippingInformationService shippingInformationService;

    private final ShippingInformationRepository shippingInformationRepository;

    private final ShippingInformationQueryService shippingInformationQueryService;

    public ShippingInformationResource(
        ShippingInformationService shippingInformationService,
        ShippingInformationRepository shippingInformationRepository,
        ShippingInformationQueryService shippingInformationQueryService
    ) {
        this.shippingInformationService = shippingInformationService;
        this.shippingInformationRepository = shippingInformationRepository;
        this.shippingInformationQueryService = shippingInformationQueryService;
    }

    /**
     * {@code POST  /shipping-informations} : Create a new shippingInformation.
     *
     * @param shippingInformation the shippingInformation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shippingInformation, or with status {@code 400 (Bad Request)} if the shippingInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShippingInformation> createShippingInformation(@RequestBody ShippingInformation shippingInformation)
        throws URISyntaxException {
        log.debug("REST request to save ShippingInformation : {}", shippingInformation);
        if (shippingInformation.getId() != null) {
            throw new BadRequestAlertException("A new shippingInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShippingInformation result = shippingInformationService.save(shippingInformation);
        return ResponseEntity
            .created(new URI("/api/shipping-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shipping-informations/:id} : Updates an existing shippingInformation.
     *
     * @param id the id of the shippingInformation to save.
     * @param shippingInformation the shippingInformation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shippingInformation,
     * or with status {@code 400 (Bad Request)} if the shippingInformation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shippingInformation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShippingInformation> updateShippingInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShippingInformation shippingInformation
    ) throws URISyntaxException {
        log.debug("REST request to update ShippingInformation : {}, {}", id, shippingInformation);
        if (shippingInformation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shippingInformation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shippingInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShippingInformation result = shippingInformationService.update(shippingInformation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shippingInformation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shipping-informations/:id} : Partial updates given fields of an existing shippingInformation, field will ignore if it is null
     *
     * @param id the id of the shippingInformation to save.
     * @param shippingInformation the shippingInformation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shippingInformation,
     * or with status {@code 400 (Bad Request)} if the shippingInformation is not valid,
     * or with status {@code 404 (Not Found)} if the shippingInformation is not found,
     * or with status {@code 500 (Internal Server Error)} if the shippingInformation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShippingInformation> partialUpdateShippingInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShippingInformation shippingInformation
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShippingInformation partially : {}, {}", id, shippingInformation);
        if (shippingInformation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shippingInformation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shippingInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShippingInformation> result = shippingInformationService.partialUpdate(shippingInformation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shippingInformation.getId().toString())
        );
    }

    /**
     * {@code GET  /shipping-informations} : get all the shippingInformations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shippingInformations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShippingInformation>> getAllShippingInformations(ShippingInformationCriteria criteria) {
        log.debug("REST request to get ShippingInformations by criteria: {}", criteria);

        List<ShippingInformation> entityList = shippingInformationQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /shipping-informations/count} : count all the shippingInformations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShippingInformations(ShippingInformationCriteria criteria) {
        log.debug("REST request to count ShippingInformations by criteria: {}", criteria);
        return ResponseEntity.ok().body(shippingInformationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipping-informations/:id} : get the "id" shippingInformation.
     *
     * @param id the id of the shippingInformation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shippingInformation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShippingInformation> getShippingInformation(@PathVariable("id") Long id) {
        log.debug("REST request to get ShippingInformation : {}", id);
        Optional<ShippingInformation> shippingInformation = shippingInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shippingInformation);
    }

    /**
     * {@code DELETE  /shipping-informations/:id} : delete the "id" shippingInformation.
     *
     * @param id the id of the shippingInformation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShippingInformation(@PathVariable("id") Long id) {
        log.debug("REST request to delete ShippingInformation : {}", id);
        shippingInformationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
