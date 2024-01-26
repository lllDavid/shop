package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShippingInformation.
 */
@Entity
@Table(name = "shipping_information")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShippingInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "shipping_status")
    private String shippingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "favoriteProducts", "carts", "shippingInformations" }, allowSetters = true)
    private Customer customer;

    @JsonIgnoreProperties(value = { "shippingInformation", "paymentMethod", "products", "customer" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "shippingInformation")
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShippingInformation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryAddress() {
        return this.deliveryAddress;
    }

    public ShippingInformation deliveryAddress(String deliveryAddress) {
        this.setDeliveryAddress(deliveryAddress);
        return this;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getShippingStatus() {
        return this.shippingStatus;
    }

    public ShippingInformation shippingStatus(String shippingStatus) {
        this.setShippingStatus(shippingStatus);
        return this;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ShippingInformation customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        if (this.order != null) {
            this.order.setShippingInformation(null);
        }
        if (order != null) {
            order.setShippingInformation(this);
        }
        this.order = order;
    }

    public ShippingInformation order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShippingInformation)) {
            return false;
        }
        return getId() != null && getId().equals(((ShippingInformation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShippingInformation{" +
            "id=" + getId() +
            ", deliveryAddress='" + getDeliveryAddress() + "'" +
            ", shippingStatus='" + getShippingStatus() + "'" +
            "}";
    }
}
