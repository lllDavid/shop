package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.OrderTestSamples.*;
import static com.mycompany.myapp.domain.PaymentMethodTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PaymentMethodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethod.class);
        PaymentMethod paymentMethod1 = getPaymentMethodSample1();
        PaymentMethod paymentMethod2 = new PaymentMethod();
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);

        paymentMethod2.setId(paymentMethod1.getId());
        assertThat(paymentMethod1).isEqualTo(paymentMethod2);

        paymentMethod2 = getPaymentMethodSample2();
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);
    }

    @Test
    void ordersTest() throws Exception {
        PaymentMethod paymentMethod = getPaymentMethodRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        paymentMethod.addOrders(orderBack);
        assertThat(paymentMethod.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getPaymentMethod()).isEqualTo(paymentMethod);

        paymentMethod.removeOrders(orderBack);
        assertThat(paymentMethod.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getPaymentMethod()).isNull();

        paymentMethod.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(paymentMethod.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getPaymentMethod()).isEqualTo(paymentMethod);

        paymentMethod.setOrders(new HashSet<>());
        assertThat(paymentMethod.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getPaymentMethod()).isNull();
    }
}
