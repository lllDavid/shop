package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "shippingInformation", "paymentMethod", "products", "customer" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_customer__favorite_products",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "favorite_products_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "orders", "favoritedByCustomers" }, allowSetters = true)
    private Set<Product> favoriteProducts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer", "product" }, allowSetters = true)
    private Set<Cart> carts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer", "order" }, allowSetters = true)
    private Set<ShippingInformation> shippingInformations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Customer lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Customer firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddress() {
        return this.address;
    }

    public Customer address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return this.email;
    }

    public Customer email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public Customer password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setCustomer(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setCustomer(this));
        }
        this.orders = orders;
    }

    public Customer orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Customer addOrders(Order order) {
        this.orders.add(order);
        order.setCustomer(this);
        return this;
    }

    public Customer removeOrders(Order order) {
        this.orders.remove(order);
        order.setCustomer(null);
        return this;
    }

    public Set<Product> getFavoriteProducts() {
        return this.favoriteProducts;
    }

    public void setFavoriteProducts(Set<Product> products) {
        this.favoriteProducts = products;
    }

    public Customer favoriteProducts(Set<Product> products) {
        this.setFavoriteProducts(products);
        return this;
    }

    public Customer addFavoriteProducts(Product product) {
        this.favoriteProducts.add(product);
        return this;
    }

    public Customer removeFavoriteProducts(Product product) {
        this.favoriteProducts.remove(product);
        return this;
    }

    public Set<Cart> getCarts() {
        return this.carts;
    }

    public void setCarts(Set<Cart> carts) {
        if (this.carts != null) {
            this.carts.forEach(i -> i.setCustomer(null));
        }
        if (carts != null) {
            carts.forEach(i -> i.setCustomer(this));
        }
        this.carts = carts;
    }

    public Customer carts(Set<Cart> carts) {
        this.setCarts(carts);
        return this;
    }

    public Customer addCarts(Cart cart) {
        this.carts.add(cart);
        cart.setCustomer(this);
        return this;
    }

    public Customer removeCarts(Cart cart) {
        this.carts.remove(cart);
        cart.setCustomer(null);
        return this;
    }

    public Set<ShippingInformation> getShippingInformations() {
        return this.shippingInformations;
    }

    public void setShippingInformations(Set<ShippingInformation> shippingInformations) {
        if (this.shippingInformations != null) {
            this.shippingInformations.forEach(i -> i.setCustomer(null));
        }
        if (shippingInformations != null) {
            shippingInformations.forEach(i -> i.setCustomer(this));
        }
        this.shippingInformations = shippingInformations;
    }

    public Customer shippingInformations(Set<ShippingInformation> shippingInformations) {
        this.setShippingInformations(shippingInformations);
        return this;
    }

    public Customer addShippingInformation(ShippingInformation shippingInformation) {
        this.shippingInformations.add(shippingInformation);
        shippingInformation.setCustomer(this);
        return this;
    }

    public Customer removeShippingInformation(ShippingInformation shippingInformation) {
        this.shippingInformations.remove(shippingInformation);
        shippingInformation.setCustomer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return getId() != null && getId().equals(((Customer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", address='" + getAddress() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
