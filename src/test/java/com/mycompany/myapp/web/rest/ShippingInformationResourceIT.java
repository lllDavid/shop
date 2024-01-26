package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.ShippingInformation;
import com.mycompany.myapp.repository.ShippingInformationRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ShippingInformationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShippingInformationResourceIT {

    private static final String DEFAULT_DELIVERY_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_SHIPPING_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPING_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shipping-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShippingInformationRepository shippingInformationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShippingInformationMockMvc;

    private ShippingInformation shippingInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShippingInformation createEntity(EntityManager em) {
        ShippingInformation shippingInformation = new ShippingInformation()
            .deliveryAddress(DEFAULT_DELIVERY_ADDRESS)
            .shippingStatus(DEFAULT_SHIPPING_STATUS);
        return shippingInformation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShippingInformation createUpdatedEntity(EntityManager em) {
        ShippingInformation shippingInformation = new ShippingInformation()
            .deliveryAddress(UPDATED_DELIVERY_ADDRESS)
            .shippingStatus(UPDATED_SHIPPING_STATUS);
        return shippingInformation;
    }

    @BeforeEach
    public void initTest() {
        shippingInformation = createEntity(em);
    }

    @Test
    @Transactional
    void createShippingInformation() throws Exception {
        int databaseSizeBeforeCreate = shippingInformationRepository.findAll().size();
        // Create the ShippingInformation
        restShippingInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingInformation))
            )
            .andExpect(status().isCreated());

        // Validate the ShippingInformation in the database
        List<ShippingInformation> shippingInformationList = shippingInformationRepository.findAll();
        assertThat(shippingInformationList).hasSize(databaseSizeBeforeCreate + 1);
        ShippingInformation testShippingInformation = shippingInformationList.get(shippingInformationList.size() - 1);
        assertThat(testShippingInformation.getDeliveryAddress()).isEqualTo(DEFAULT_DELIVERY_ADDRESS);
        assertThat(testShippingInformation.getShippingStatus()).isEqualTo(DEFAULT_SHIPPING_STATUS);
    }

    @Test
    @Transactional
    void createShippingInformationWithExistingId() throws Exception {
        // Create the ShippingInformation with an existing ID
        shippingInformation.setId(1L);

        int databaseSizeBeforeCreate = shippingInformationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShippingInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingInformation in the database
        List<ShippingInformation> shippingInformationList = shippingInformationRepository.findAll();
        assertThat(shippingInformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShippingInformations() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        // Get all the shippingInformationList
        restShippingInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shippingInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].deliveryAddress").value(hasItem(DEFAULT_DELIVERY_ADDRESS)))
            .andExpect(jsonPath("$.[*].shippingStatus").value(hasItem(DEFAULT_SHIPPING_STATUS)));
    }

    @Test
    @Transactional
    void getShippingInformation() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        // Get the shippingInformation
        restShippingInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, shippingInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shippingInformation.getId().intValue()))
            .andExpect(jsonPath("$.deliveryAddress").value(DEFAULT_DELIVERY_ADDRESS))
            .andExpect(jsonPath("$.shippingStatus").value(DEFAULT_SHIPPING_STATUS));
    }

    @Test
    @Transactional
    void getShippingInformationsByIdFiltering() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        Long id = shippingInformation.getId();

        defaultShippingInformationShouldBeFound("id.equals=" + id);
        defaultShippingInformationShouldNotBeFound("id.notEquals=" + id);

        defaultShippingInformationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultShippingInformationShouldNotBeFound("id.greaterThan=" + id);

        defaultShippingInformationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultShippingInformationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShippingInformationsByDeliveryAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        // Get all the shippingInformationList where deliveryAddress equals to DEFAULT_DELIVERY_ADDRESS
        defaultShippingInformationShouldBeFound("deliveryAddress.equals=" + DEFAULT_DELIVERY_ADDRESS);

        // Get all the shippingInformationList where deliveryAddress equals to UPDATED_DELIVERY_ADDRESS
        defaultShippingInformationShouldNotBeFound("deliveryAddress.equals=" + UPDATED_DELIVERY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllShippingInformationsByDeliveryAddressIsInShouldWork() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        // Get all the shippingInformationList where deliveryAddress in DEFAULT_DELIVERY_ADDRESS or UPDATED_DELIVERY_ADDRESS
        defaultShippingInformationShouldBeFound("deliveryAddress.in=" + DEFAULT_DELIVERY_ADDRESS + "," + UPDATED_DELIVERY_ADDRESS);

        // Get all the shippingInformationList where deliveryAddress equals to UPDATED_DELIVERY_ADDRESS
        defaultShippingInformationShouldNotBeFound("deliveryAddress.in=" + UPDATED_DELIVERY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllShippingInformationsByDeliveryAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        // Get all the shippingInformationList where deliveryAddress is not null
        defaultShippingInformationShouldBeFound("deliveryAddress.specified=true");

        // Get all the shippingInformationList where deliveryAddress is null
        defaultShippingInformationShouldNotBeFound("deliveryAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingInformationsByDeliveryAddressContainsSomething() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        // Get all the shippingInformationList where deliveryAddress contains DEFAULT_DELIVERY_ADDRESS
        defaultShippingInformationShouldBeFound("deliveryAddress.contains=" + DEFAULT_DELIVERY_ADDRESS);

        // Get all the shippingInformationList where deliveryAddress contains UPDATED_DELIVERY_ADDRESS
        defaultShippingInformationShouldNotBeFound("deliveryAddress.contains=" + UPDATED_DELIVERY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllShippingInformationsByDeliveryAddressNotContainsSomething() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        // Get all the shippingInformationList where deliveryAddress does not contain DEFAULT_DELIVERY_ADDRESS
        defaultShippingInformationShouldNotBeFound("deliveryAddress.doesNotContain=" + DEFAULT_DELIVERY_ADDRESS);

        // Get all the shippingInformationList where deliveryAddress does not contain UPDATED_DELIVERY_ADDRESS
        defaultShippingInformationShouldBeFound("deliveryAddress.doesNotContain=" + UPDATED_DELIVERY_ADDRESS);
    }

    @Test
    @Transactional
    void getAllShippingInformationsByShippingStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        // Get all the shippingInformationList where shippingStatus equals to DEFAULT_SHIPPING_STATUS
        defaultShippingInformationShouldBeFound("shippingStatus.equals=" + DEFAULT_SHIPPING_STATUS);

        // Get all the shippingInformationList where shippingStatus equals to UPDATED_SHIPPING_STATUS
        defaultShippingInformationShouldNotBeFound("shippingStatus.equals=" + UPDATED_SHIPPING_STATUS);
    }

    @Test
    @Transactional
    void getAllShippingInformationsByShippingStatusIsInShouldWork() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        // Get all the shippingInformationList where shippingStatus in DEFAULT_SHIPPING_STATUS or UPDATED_SHIPPING_STATUS
        defaultShippingInformationShouldBeFound("shippingStatus.in=" + DEFAULT_SHIPPING_STATUS + "," + UPDATED_SHIPPING_STATUS);

        // Get all the shippingInformationList where shippingStatus equals to UPDATED_SHIPPING_STATUS
        defaultShippingInformationShouldNotBeFound("shippingStatus.in=" + UPDATED_SHIPPING_STATUS);
    }

    @Test
    @Transactional
    void getAllShippingInformationsByShippingStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        // Get all the shippingInformationList where shippingStatus is not null
        defaultShippingInformationShouldBeFound("shippingStatus.specified=true");

        // Get all the shippingInformationList where shippingStatus is null
        defaultShippingInformationShouldNotBeFound("shippingStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingInformationsByShippingStatusContainsSomething() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        // Get all the shippingInformationList where shippingStatus contains DEFAULT_SHIPPING_STATUS
        defaultShippingInformationShouldBeFound("shippingStatus.contains=" + DEFAULT_SHIPPING_STATUS);

        // Get all the shippingInformationList where shippingStatus contains UPDATED_SHIPPING_STATUS
        defaultShippingInformationShouldNotBeFound("shippingStatus.contains=" + UPDATED_SHIPPING_STATUS);
    }

    @Test
    @Transactional
    void getAllShippingInformationsByShippingStatusNotContainsSomething() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        // Get all the shippingInformationList where shippingStatus does not contain DEFAULT_SHIPPING_STATUS
        defaultShippingInformationShouldNotBeFound("shippingStatus.doesNotContain=" + DEFAULT_SHIPPING_STATUS);

        // Get all the shippingInformationList where shippingStatus does not contain UPDATED_SHIPPING_STATUS
        defaultShippingInformationShouldBeFound("shippingStatus.doesNotContain=" + UPDATED_SHIPPING_STATUS);
    }

    @Test
    @Transactional
    void getAllShippingInformationsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            shippingInformationRepository.saveAndFlush(shippingInformation);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        shippingInformation.setCustomer(customer);
        shippingInformationRepository.saveAndFlush(shippingInformation);
        Long customerId = customer.getId();
        // Get all the shippingInformationList where customer equals to customerId
        defaultShippingInformationShouldBeFound("customerId.equals=" + customerId);

        // Get all the shippingInformationList where customer equals to (customerId + 1)
        defaultShippingInformationShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllShippingInformationsByOrderIsEqualToSomething() throws Exception {
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            shippingInformationRepository.saveAndFlush(shippingInformation);
            order = OrderResourceIT.createEntity(em);
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        em.persist(order);
        em.flush();
        shippingInformation.setOrder(order);
        order.setShippingInformation(shippingInformation);
        shippingInformationRepository.saveAndFlush(shippingInformation);
        Long orderId = order.getId();
        // Get all the shippingInformationList where order equals to orderId
        defaultShippingInformationShouldBeFound("orderId.equals=" + orderId);

        // Get all the shippingInformationList where order equals to (orderId + 1)
        defaultShippingInformationShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShippingInformationShouldBeFound(String filter) throws Exception {
        restShippingInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shippingInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].deliveryAddress").value(hasItem(DEFAULT_DELIVERY_ADDRESS)))
            .andExpect(jsonPath("$.[*].shippingStatus").value(hasItem(DEFAULT_SHIPPING_STATUS)));

        // Check, that the count call also returns 1
        restShippingInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShippingInformationShouldNotBeFound(String filter) throws Exception {
        restShippingInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShippingInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShippingInformation() throws Exception {
        // Get the shippingInformation
        restShippingInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShippingInformation() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        int databaseSizeBeforeUpdate = shippingInformationRepository.findAll().size();

        // Update the shippingInformation
        ShippingInformation updatedShippingInformation = shippingInformationRepository.findById(shippingInformation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShippingInformation are not directly saved in db
        em.detach(updatedShippingInformation);
        updatedShippingInformation.deliveryAddress(UPDATED_DELIVERY_ADDRESS).shippingStatus(UPDATED_SHIPPING_STATUS);

        restShippingInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShippingInformation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedShippingInformation))
            )
            .andExpect(status().isOk());

        // Validate the ShippingInformation in the database
        List<ShippingInformation> shippingInformationList = shippingInformationRepository.findAll();
        assertThat(shippingInformationList).hasSize(databaseSizeBeforeUpdate);
        ShippingInformation testShippingInformation = shippingInformationList.get(shippingInformationList.size() - 1);
        assertThat(testShippingInformation.getDeliveryAddress()).isEqualTo(UPDATED_DELIVERY_ADDRESS);
        assertThat(testShippingInformation.getShippingStatus()).isEqualTo(UPDATED_SHIPPING_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingShippingInformation() throws Exception {
        int databaseSizeBeforeUpdate = shippingInformationRepository.findAll().size();
        shippingInformation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shippingInformation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingInformation in the database
        List<ShippingInformation> shippingInformationList = shippingInformationRepository.findAll();
        assertThat(shippingInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShippingInformation() throws Exception {
        int databaseSizeBeforeUpdate = shippingInformationRepository.findAll().size();
        shippingInformation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingInformation in the database
        List<ShippingInformation> shippingInformationList = shippingInformationRepository.findAll();
        assertThat(shippingInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShippingInformation() throws Exception {
        int databaseSizeBeforeUpdate = shippingInformationRepository.findAll().size();
        shippingInformation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingInformationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingInformation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShippingInformation in the database
        List<ShippingInformation> shippingInformationList = shippingInformationRepository.findAll();
        assertThat(shippingInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShippingInformationWithPatch() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        int databaseSizeBeforeUpdate = shippingInformationRepository.findAll().size();

        // Update the shippingInformation using partial update
        ShippingInformation partialUpdatedShippingInformation = new ShippingInformation();
        partialUpdatedShippingInformation.setId(shippingInformation.getId());

        partialUpdatedShippingInformation.deliveryAddress(UPDATED_DELIVERY_ADDRESS);

        restShippingInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShippingInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShippingInformation))
            )
            .andExpect(status().isOk());

        // Validate the ShippingInformation in the database
        List<ShippingInformation> shippingInformationList = shippingInformationRepository.findAll();
        assertThat(shippingInformationList).hasSize(databaseSizeBeforeUpdate);
        ShippingInformation testShippingInformation = shippingInformationList.get(shippingInformationList.size() - 1);
        assertThat(testShippingInformation.getDeliveryAddress()).isEqualTo(UPDATED_DELIVERY_ADDRESS);
        assertThat(testShippingInformation.getShippingStatus()).isEqualTo(DEFAULT_SHIPPING_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateShippingInformationWithPatch() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        int databaseSizeBeforeUpdate = shippingInformationRepository.findAll().size();

        // Update the shippingInformation using partial update
        ShippingInformation partialUpdatedShippingInformation = new ShippingInformation();
        partialUpdatedShippingInformation.setId(shippingInformation.getId());

        partialUpdatedShippingInformation.deliveryAddress(UPDATED_DELIVERY_ADDRESS).shippingStatus(UPDATED_SHIPPING_STATUS);

        restShippingInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShippingInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShippingInformation))
            )
            .andExpect(status().isOk());

        // Validate the ShippingInformation in the database
        List<ShippingInformation> shippingInformationList = shippingInformationRepository.findAll();
        assertThat(shippingInformationList).hasSize(databaseSizeBeforeUpdate);
        ShippingInformation testShippingInformation = shippingInformationList.get(shippingInformationList.size() - 1);
        assertThat(testShippingInformation.getDeliveryAddress()).isEqualTo(UPDATED_DELIVERY_ADDRESS);
        assertThat(testShippingInformation.getShippingStatus()).isEqualTo(UPDATED_SHIPPING_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingShippingInformation() throws Exception {
        int databaseSizeBeforeUpdate = shippingInformationRepository.findAll().size();
        shippingInformation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shippingInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shippingInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingInformation in the database
        List<ShippingInformation> shippingInformationList = shippingInformationRepository.findAll();
        assertThat(shippingInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShippingInformation() throws Exception {
        int databaseSizeBeforeUpdate = shippingInformationRepository.findAll().size();
        shippingInformation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shippingInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingInformation in the database
        List<ShippingInformation> shippingInformationList = shippingInformationRepository.findAll();
        assertThat(shippingInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShippingInformation() throws Exception {
        int databaseSizeBeforeUpdate = shippingInformationRepository.findAll().size();
        shippingInformation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingInformationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shippingInformation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShippingInformation in the database
        List<ShippingInformation> shippingInformationList = shippingInformationRepository.findAll();
        assertThat(shippingInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShippingInformation() throws Exception {
        // Initialize the database
        shippingInformationRepository.saveAndFlush(shippingInformation);

        int databaseSizeBeforeDelete = shippingInformationRepository.findAll().size();

        // Delete the shippingInformation
        restShippingInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, shippingInformation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShippingInformation> shippingInformationList = shippingInformationRepository.findAll();
        assertThat(shippingInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
