package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cart;
import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.repository.CartRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CartResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CartResourceIT {

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PRODUCT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRODUCT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRODUCT_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/carts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCartMockMvc;

    private Cart cart;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cart createEntity(EntityManager em) {
        Cart cart = new Cart().totalPrice(DEFAULT_TOTAL_PRICE).productAmount(DEFAULT_PRODUCT_AMOUNT);
        return cart;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cart createUpdatedEntity(EntityManager em) {
        Cart cart = new Cart().totalPrice(UPDATED_TOTAL_PRICE).productAmount(UPDATED_PRODUCT_AMOUNT);
        return cart;
    }

    @BeforeEach
    public void initTest() {
        cart = createEntity(em);
    }

    @Test
    @Transactional
    void createCart() throws Exception {
        int databaseSizeBeforeCreate = cartRepository.findAll().size();
        // Create the Cart
        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cart)))
            .andExpect(status().isCreated());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeCreate + 1);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getTotalPrice()).isEqualByComparingTo(DEFAULT_TOTAL_PRICE);
        assertThat(testCart.getProductAmount()).isEqualByComparingTo(DEFAULT_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void createCartWithExistingId() throws Exception {
        // Create the Cart with an existing ID
        cart.setId(1L);

        int databaseSizeBeforeCreate = cartRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cart)))
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCarts() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cart.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].productAmount").value(hasItem(sameNumber(DEFAULT_PRODUCT_AMOUNT))));
    }

    @Test
    @Transactional
    void getCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get the cart
        restCartMockMvc
            .perform(get(ENTITY_API_URL_ID, cart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cart.getId().intValue()))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.productAmount").value(sameNumber(DEFAULT_PRODUCT_AMOUNT)));
    }

    @Test
    @Transactional
    void getCartsByIdFiltering() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        Long id = cart.getId();

        defaultCartShouldBeFound("id.equals=" + id);
        defaultCartShouldNotBeFound("id.notEquals=" + id);

        defaultCartShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCartShouldNotBeFound("id.greaterThan=" + id);

        defaultCartShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCartShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the cartList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is not null
        defaultCartShouldBeFound("totalPrice.specified=true");

        // Get all the cartList where totalPrice is null
        defaultCartShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is greater than or equal to DEFAULT_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice is greater than or equal to UPDATED_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is less than or equal to DEFAULT_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice is less than or equal to SMALLER_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is less than DEFAULT_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice is less than UPDATED_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where totalPrice is greater than DEFAULT_TOTAL_PRICE
        defaultCartShouldNotBeFound("totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the cartList where totalPrice is greater than SMALLER_TOTAL_PRICE
        defaultCartShouldBeFound("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllCartsByProductAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where productAmount equals to DEFAULT_PRODUCT_AMOUNT
        defaultCartShouldBeFound("productAmount.equals=" + DEFAULT_PRODUCT_AMOUNT);

        // Get all the cartList where productAmount equals to UPDATED_PRODUCT_AMOUNT
        defaultCartShouldNotBeFound("productAmount.equals=" + UPDATED_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCartsByProductAmountIsInShouldWork() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where productAmount in DEFAULT_PRODUCT_AMOUNT or UPDATED_PRODUCT_AMOUNT
        defaultCartShouldBeFound("productAmount.in=" + DEFAULT_PRODUCT_AMOUNT + "," + UPDATED_PRODUCT_AMOUNT);

        // Get all the cartList where productAmount equals to UPDATED_PRODUCT_AMOUNT
        defaultCartShouldNotBeFound("productAmount.in=" + UPDATED_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCartsByProductAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where productAmount is not null
        defaultCartShouldBeFound("productAmount.specified=true");

        // Get all the cartList where productAmount is null
        defaultCartShouldNotBeFound("productAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllCartsByProductAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where productAmount is greater than or equal to DEFAULT_PRODUCT_AMOUNT
        defaultCartShouldBeFound("productAmount.greaterThanOrEqual=" + DEFAULT_PRODUCT_AMOUNT);

        // Get all the cartList where productAmount is greater than or equal to UPDATED_PRODUCT_AMOUNT
        defaultCartShouldNotBeFound("productAmount.greaterThanOrEqual=" + UPDATED_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCartsByProductAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where productAmount is less than or equal to DEFAULT_PRODUCT_AMOUNT
        defaultCartShouldBeFound("productAmount.lessThanOrEqual=" + DEFAULT_PRODUCT_AMOUNT);

        // Get all the cartList where productAmount is less than or equal to SMALLER_PRODUCT_AMOUNT
        defaultCartShouldNotBeFound("productAmount.lessThanOrEqual=" + SMALLER_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCartsByProductAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where productAmount is less than DEFAULT_PRODUCT_AMOUNT
        defaultCartShouldNotBeFound("productAmount.lessThan=" + DEFAULT_PRODUCT_AMOUNT);

        // Get all the cartList where productAmount is less than UPDATED_PRODUCT_AMOUNT
        defaultCartShouldBeFound("productAmount.lessThan=" + UPDATED_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCartsByProductAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        // Get all the cartList where productAmount is greater than DEFAULT_PRODUCT_AMOUNT
        defaultCartShouldNotBeFound("productAmount.greaterThan=" + DEFAULT_PRODUCT_AMOUNT);

        // Get all the cartList where productAmount is greater than SMALLER_PRODUCT_AMOUNT
        defaultCartShouldBeFound("productAmount.greaterThan=" + SMALLER_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCartsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            cartRepository.saveAndFlush(cart);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        cart.setCustomer(customer);
        cartRepository.saveAndFlush(cart);
        Long customerId = customer.getId();
        // Get all the cartList where customer equals to customerId
        defaultCartShouldBeFound("customerId.equals=" + customerId);

        // Get all the cartList where customer equals to (customerId + 1)
        defaultCartShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllCartsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            cartRepository.saveAndFlush(cart);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        cart.setProduct(product);
        cartRepository.saveAndFlush(cart);
        Long productId = product.getId();
        // Get all the cartList where product equals to productId
        defaultCartShouldBeFound("productId.equals=" + productId);

        // Get all the cartList where product equals to (productId + 1)
        defaultCartShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCartShouldBeFound(String filter) throws Exception {
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cart.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].productAmount").value(hasItem(sameNumber(DEFAULT_PRODUCT_AMOUNT))));

        // Check, that the count call also returns 1
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCartShouldNotBeFound(String filter) throws Exception {
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCart() throws Exception {
        // Get the cart
        restCartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Update the cart
        Cart updatedCart = cartRepository.findById(cart.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCart are not directly saved in db
        em.detach(updatedCart);
        updatedCart.totalPrice(UPDATED_TOTAL_PRICE).productAmount(UPDATED_PRODUCT_AMOUNT);

        restCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCart.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCart))
            )
            .andExpect(status().isOk());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getTotalPrice()).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
        assertThat(testCart.getProductAmount()).isEqualByComparingTo(UPDATED_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cart.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cart))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cart))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cart)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCartWithPatch() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Update the cart using partial update
        Cart partialUpdatedCart = new Cart();
        partialUpdatedCart.setId(cart.getId());

        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCart))
            )
            .andExpect(status().isOk());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getTotalPrice()).isEqualByComparingTo(DEFAULT_TOTAL_PRICE);
        assertThat(testCart.getProductAmount()).isEqualByComparingTo(DEFAULT_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateCartWithPatch() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        int databaseSizeBeforeUpdate = cartRepository.findAll().size();

        // Update the cart using partial update
        Cart partialUpdatedCart = new Cart();
        partialUpdatedCart.setId(cart.getId());

        partialUpdatedCart.totalPrice(UPDATED_TOTAL_PRICE).productAmount(UPDATED_PRODUCT_AMOUNT);

        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCart))
            )
            .andExpect(status().isOk());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
        Cart testCart = cartList.get(cartList.size() - 1);
        assertThat(testCart.getTotalPrice()).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
        assertThat(testCart.getProductAmount()).isEqualByComparingTo(UPDATED_PRODUCT_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cart))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cart))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCart() throws Exception {
        int databaseSizeBeforeUpdate = cartRepository.findAll().size();
        cart.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCartMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cart)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cart in the database
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCart() throws Exception {
        // Initialize the database
        cartRepository.saveAndFlush(cart);

        int databaseSizeBeforeDelete = cartRepository.findAll().size();

        // Delete the cart
        restCartMockMvc
            .perform(delete(ENTITY_API_URL_ID, cart.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cart> cartList = cartRepository.findAll();
        assertThat(cartList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
