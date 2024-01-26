package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CategoryTestSamples.*;
import static com.mycompany.myapp.domain.CustomerTestSamples.*;
import static com.mycompany.myapp.domain.OrderTestSamples.*;
import static com.mycompany.myapp.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void categoryTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.setCategory(categoryBack);
        assertThat(product.getCategory()).isEqualTo(categoryBack);

        product.category(null);
        assertThat(product.getCategory()).isNull();
    }

    @Test
    void ordersTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        product.addOrders(orderBack);
        assertThat(product.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getProducts()).containsOnly(product);

        product.removeOrders(orderBack);
        assertThat(product.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getProducts()).doesNotContain(product);

        product.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(product.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getProducts()).containsOnly(product);

        product.setOrders(new HashSet<>());
        assertThat(product.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getProducts()).doesNotContain(product);
    }

    @Test
    void favoritedByCustomersTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        product.addFavoritedByCustomers(customerBack);
        assertThat(product.getFavoritedByCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getFavoriteProducts()).containsOnly(product);

        product.removeFavoritedByCustomers(customerBack);
        assertThat(product.getFavoritedByCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getFavoriteProducts()).doesNotContain(product);

        product.favoritedByCustomers(new HashSet<>(Set.of(customerBack)));
        assertThat(product.getFavoritedByCustomers()).containsOnly(customerBack);
        assertThat(customerBack.getFavoriteProducts()).containsOnly(product);

        product.setFavoritedByCustomers(new HashSet<>());
        assertThat(product.getFavoritedByCustomers()).doesNotContain(customerBack);
        assertThat(customerBack.getFavoriteProducts()).doesNotContain(product);
    }
}
