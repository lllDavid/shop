package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShippingInformationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ShippingInformation getShippingInformationSample1() {
        return new ShippingInformation().id(1L).deliveryAddress("deliveryAddress1").shippingStatus("shippingStatus1");
    }

    public static ShippingInformation getShippingInformationSample2() {
        return new ShippingInformation().id(2L).deliveryAddress("deliveryAddress2").shippingStatus("shippingStatus2");
    }

    public static ShippingInformation getShippingInformationRandomSampleGenerator() {
        return new ShippingInformation()
            .id(longCount.incrementAndGet())
            .deliveryAddress(UUID.randomUUID().toString())
            .shippingStatus(UUID.randomUUID().toString());
    }
}
