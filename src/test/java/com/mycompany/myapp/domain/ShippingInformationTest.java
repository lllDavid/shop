package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CustomerTestSamples.*;
import static com.mycompany.myapp.domain.OrderTestSamples.*;
import static com.mycompany.myapp.domain.ShippingInformationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShippingInformationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShippingInformation.class);
        ShippingInformation shippingInformation1 = getShippingInformationSample1();
        ShippingInformation shippingInformation2 = new ShippingInformation();
        assertThat(shippingInformation1).isNotEqualTo(shippingInformation2);

        shippingInformation2.setId(shippingInformation1.getId());
        assertThat(shippingInformation1).isEqualTo(shippingInformation2);

        shippingInformation2 = getShippingInformationSample2();
        assertThat(shippingInformation1).isNotEqualTo(shippingInformation2);
    }

    @Test
    void customerTest() throws Exception {
        ShippingInformation shippingInformation = getShippingInformationRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        shippingInformation.setCustomer(customerBack);
        assertThat(shippingInformation.getCustomer()).isEqualTo(customerBack);

        shippingInformation.customer(null);
        assertThat(shippingInformation.getCustomer()).isNull();
    }

    @Test
    void orderTest() throws Exception {
        ShippingInformation shippingInformation = getShippingInformationRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        shippingInformation.setOrder(orderBack);
        assertThat(shippingInformation.getOrder()).isEqualTo(orderBack);
        assertThat(orderBack.getShippingInformation()).isEqualTo(shippingInformation);

        shippingInformation.order(null);
        assertThat(shippingInformation.getOrder()).isNull();
        assertThat(orderBack.getShippingInformation()).isNull();
    }
}
