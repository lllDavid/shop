package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "in_stock")
    private Integer inStock;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne(fetch = FetchType.EAGER) // Changed lazy to eager
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "shippingInformation", "paymentMethod", "products", "customer" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favoriteProducts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "orders", "favoriteProducts", "carts", "shippingInformations" }, allowSetters = true)
    private Set<Customer> favoritedByCustomers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getInStock() {
        return this.inStock;
    }

    public Product inStock(Integer inStock) {
        this.setInStock(inStock);
        return this;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Product image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Product imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.removeProducts(this));
        }
        if (orders != null) {
            orders.forEach(i -> i.addProducts(this));
        }
        this.orders = orders;
    }

    public Product orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Product addOrders(Order order) {
        this.orders.add(order);
        order.getProducts().add(this);
        return this;
    }

    public Product removeOrders(Order order) {
        this.orders.remove(order);
        order.getProducts().remove(this);
        return this;
    }

    public Set<Customer> getFavoritedByCustomers() {
        return this.favoritedByCustomers;
    }

    public void setFavoritedByCustomers(Set<Customer> customers) {
        if (this.favoritedByCustomers != null) {
            this.favoritedByCustomers.forEach(i -> i.removeFavoriteProducts(this));
        }
        if (customers != null) {
            customers.forEach(i -> i.addFavoriteProducts(this));
        }
        this.favoritedByCustomers = customers;
    }

    public Product favoritedByCustomers(Set<Customer> customers) {
        this.setFavoritedByCustomers(customers);
        return this;
    }

    public Product addFavoritedByCustomers(Customer customer) {
        this.favoritedByCustomers.add(customer);
        customer.getFavoriteProducts().add(this);
        return this;
    }

    public Product removeFavoritedByCustomers(Customer customer) {
        this.favoritedByCustomers.remove(customer);
        customer.getFavoriteProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", inStock=" + getInStock() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
