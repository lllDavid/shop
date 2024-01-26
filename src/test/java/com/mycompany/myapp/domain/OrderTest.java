package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CustomerTestSamples.*;
import static com.mycompany.myapp.domain.OrderTestSamples.*;
import static com.mycompany.myapp.domain.PaymentMethodTestSamples.*;
import static com.mycompany.myapp.domain.ProductTestSamples.*;
import static com.mycompany.myapp.domain.ShippingInformationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void shippingInformationTest() throws Exception {
        Order order = getOrderRandomSampleGenerator();
        ShippingInformation shippingInformationBack = getShippingInformationRandomSampleGenerator();

        order.setShippingInformation(shippingInformationBack);
        assertThat(order.getShippingInformation()).isEqualTo(shippingInformationBack);

        order.shippingInformation(null);
        assertThat(order.getShippingInformation()).isNull();
    }

    @Test
    void paymentMethodTest() throws Exception {
        Order order = getOrderRandomSampleGenerator();
        PaymentMethod paymentMethodBack = getPaymentMethodRandomSampleGenerator();

        order.setPaymentMethod(paymentMethodBack);
        assertThat(order.getPaymentMethod()).isEqualTo(paymentMethodBack);

        order.paymentMethod(null);
        assertThat(order.getPaymentMethod()).isNull();
    }

    @Test
    void productsTest() throws Exception {
        Order order = getOrderRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        order.addProducts(productBack);
        assertThat(order.getProducts()).containsOnly(productBack);

        order.removeProducts(productBack);
        assertThat(order.getProducts()).doesNotContain(productBack);

        order.products(new HashSet<>(Set.of(productBack)));
        assertThat(order.getProducts()).containsOnly(productBack);

        order.setProducts(new HashSet<>());
        assertThat(order.getProducts()).doesNotContain(productBack);
    }

    @Test
    void customerTest() throws Exception {
        Order order = getOrderRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        order.setCustomer(customerBack);
        assertThat(order.getCustomer()).isEqualTo(customerBack);

        order.customer(null);
        assertThat(order.getCustomer()).isNull();
    }
}
