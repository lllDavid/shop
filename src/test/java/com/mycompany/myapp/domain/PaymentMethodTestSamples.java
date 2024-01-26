package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentMethodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentMethod getPaymentMethodSample1() {
        return new PaymentMethod().id(1L).name("name1").description("description1");
    }

    public static PaymentMethod getPaymentMethodSample2() {
        return new PaymentMethod().id(2L).name("name2").description("description2");
    }

    public static PaymentMethod getPaymentMethodRandomSampleGenerator() {
        return new PaymentMethod()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
