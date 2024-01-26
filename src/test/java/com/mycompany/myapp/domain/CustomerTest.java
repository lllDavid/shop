package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CartTestSamples.*;
import static com.mycompany.myapp.domain.CustomerTestSamples.*;
import static com.mycompany.myapp.domain.OrderTestSamples.*;
import static com.mycompany.myapp.domain.ProductTestSamples.*;
import static com.mycompany.myapp.domain.ShippingInformationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void ordersTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        customer.addOrders(orderBack);
        assertThat(customer.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getCustomer()).isEqualTo(customer);

        customer.removeOrders(orderBack);
        assertThat(customer.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getCustomer()).isNull();

        customer.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(customer.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getCustomer()).isEqualTo(customer);

        customer.setOrders(new HashSet<>());
        assertThat(customer.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getCustomer()).isNull();
    }

    @Test
    void favoriteProductsTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        customer.addFavoriteProducts(productBack);
        assertThat(customer.getFavoriteProducts()).containsOnly(productBack);

        customer.removeFavoriteProducts(productBack);
        assertThat(customer.getFavoriteProducts()).doesNotContain(productBack);

        customer.favoriteProducts(new HashSet<>(Set.of(productBack)));
        assertThat(customer.getFavoriteProducts()).containsOnly(productBack);

        customer.setFavoriteProducts(new HashSet<>());
        assertThat(customer.getFavoriteProducts()).doesNotContain(productBack);
    }

    @Test
    void cartsTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        Cart cartBack = getCartRandomSampleGenerator();

        customer.addCarts(cartBack);
        assertThat(customer.getCarts()).containsOnly(cartBack);
        assertThat(cartBack.getCustomer()).isEqualTo(customer);

        customer.removeCarts(cartBack);
        assertThat(customer.getCarts()).doesNotContain(cartBack);
        assertThat(cartBack.getCustomer()).isNull();

        customer.carts(new HashSet<>(Set.of(cartBack)));
        assertThat(customer.getCarts()).containsOnly(cartBack);
        assertThat(cartBack.getCustomer()).isEqualTo(customer);

        customer.setCarts(new HashSet<>());
        assertThat(customer.getCarts()).doesNotContain(cartBack);
        assertThat(cartBack.getCustomer()).isNull();
    }

    @Test
    void shippingInformationTest() throws Exception {
        Customer customer = getCustomerRandomSampleGenerator();
        ShippingInformation shippingInformationBack = getShippingInformationRandomSampleGenerator();

        customer.addShippingInformation(shippingInformationBack);
        assertThat(customer.getShippingInformations()).containsOnly(shippingInformationBack);
        assertThat(shippingInformationBack.getCustomer()).isEqualTo(customer);

        customer.removeShippingInformation(shippingInformationBack);
        assertThat(customer.getShippingInformations()).doesNotContain(shippingInformationBack);
        assertThat(shippingInformationBack.getCustomer()).isNull();

        customer.shippingInformations(new HashSet<>(Set.of(shippingInformationBack)));
        assertThat(customer.getShippingInformations()).containsOnly(shippingInformationBack);
        assertThat(shippingInformationBack.getCustomer()).isEqualTo(customer);

        customer.setShippingInformations(new HashSet<>());
        assertThat(customer.getShippingInformations()).doesNotContain(shippingInformationBack);
        assertThat(shippingInformationBack.getCustomer()).isNull();
    }
}
