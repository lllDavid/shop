package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Product} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private BigDecimalFilter price;

    private IntegerFilter inStock;

    private LongFilter categoryId;

    private LongFilter ordersId;

    private LongFilter favoritedByCustomersId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.inStock = other.inStock == null ? null : other.inStock.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.ordersId = other.ordersId == null ? null : other.ordersId.copy();
        this.favoritedByCustomersId = other.favoritedByCustomersId == null ? null : other.favoritedByCustomersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public BigDecimalFilter price() {
        if (price == null) {
            price = new BigDecimalFilter();
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public IntegerFilter getInStock() {
        return inStock;
    }

    public IntegerFilter inStock() {
        if (inStock == null) {
            inStock = new IntegerFilter();
        }
        return inStock;
    }

    public void setInStock(IntegerFilter inStock) {
        this.inStock = inStock;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getOrdersId() {
        return ordersId;
    }

    public LongFilter ordersId() {
        if (ordersId == null) {
            ordersId = new LongFilter();
        }
        return ordersId;
    }

    public void setOrdersId(LongFilter ordersId) {
        this.ordersId = ordersId;
    }

    public LongFilter getFavoritedByCustomersId() {
        return favoritedByCustomersId;
    }

    public LongFilter favoritedByCustomersId() {
        if (favoritedByCustomersId == null) {
            favoritedByCustomersId = new LongFilter();
        }
        return favoritedByCustomersId;
    }

    public void setFavoritedByCustomersId(LongFilter favoritedByCustomersId) {
        this.favoritedByCustomersId = favoritedByCustomersId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(price, that.price) &&
            Objects.equals(inStock, that.inStock) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(ordersId, that.ordersId) &&
            Objects.equals(favoritedByCustomersId, that.favoritedByCustomersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, inStock, categoryId, ordersId, favoritedByCustomersId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (inStock != null ? "inStock=" + inStock + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (ordersId != null ? "ordersId=" + ordersId + ", " : "") +
            (favoritedByCustomersId != null ? "favoritedByCustomersId=" + favoritedByCustomersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
