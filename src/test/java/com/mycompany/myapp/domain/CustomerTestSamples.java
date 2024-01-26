package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer()
            .id(1L)
            .lastName("lastName1")
            .firstName("firstName1")
            .address("address1")
            .email("email1")
            .password("password1");
    }

    public static Customer getCustomerSample2() {
        return new Customer()
            .id(2L)
            .lastName("lastName2")
            .firstName("firstName2")
            .address("address2")
            .email("email2")
            .password("password2");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .lastName(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString());
    }
}
