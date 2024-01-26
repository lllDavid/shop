package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Order} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.OrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter date;

    private BigDecimalFilter totalPrice;

    private LongFilter shippingInformationId;

    private LongFilter paymentMethodId;

    private LongFilter productsId;

    private LongFilter customerId;

    private Boolean distinct;

    public OrderCriteria() {}

    public OrderCriteria(OrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.totalPrice = other.totalPrice == null ? null : other.totalPrice.copy();
        this.shippingInformationId = other.shippingInformationId == null ? null : other.shippingInformationId.copy();
        this.paymentMethodId = other.paymentMethodId == null ? null : other.paymentMethodId.copy();
        this.productsId = other.productsId == null ? null : other.productsId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrderCriteria copy() {
        return new OrderCriteria(this);
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

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public ZonedDateTimeFilter date() {
        if (date == null) {
            date = new ZonedDateTimeFilter();
        }
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public BigDecimalFilter getTotalPrice() {
        return totalPrice;
    }

    public BigDecimalFilter totalPrice() {
        if (totalPrice == null) {
            totalPrice = new BigDecimalFilter();
        }
        return totalPrice;
    }

    public void setTotalPrice(BigDecimalFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LongFilter getShippingInformationId() {
        return shippingInformationId;
    }

    public LongFilter shippingInformationId() {
        if (shippingInformationId == null) {
            shippingInformationId = new LongFilter();
        }
        return shippingInformationId;
    }

    public void setShippingInformationId(LongFilter shippingInformationId) {
        this.shippingInformationId = shippingInformationId;
    }

    public LongFilter getPaymentMethodId() {
        return paymentMethodId;
    }

    public LongFilter paymentMethodId() {
        if (paymentMethodId == null) {
            paymentMethodId = new LongFilter();
        }
        return paymentMethodId;
    }

    public void setPaymentMethodId(LongFilter paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public LongFilter getProductsId() {
        return productsId;
    }

    public LongFilter productsId() {
        if (productsId == null) {
            productsId = new LongFilter();
        }
        return productsId;
    }

    public void setProductsId(LongFilter productsId) {
        this.productsId = productsId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
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
        final OrderCriteria that = (OrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(shippingInformationId, that.shippingInformationId) &&
            Objects.equals(paymentMethodId, that.paymentMethodId) &&
            Objects.equals(productsId, that.productsId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, totalPrice, shippingInformationId, paymentMethodId, productsId, customerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
            (shippingInformationId != null ? "shippingInformationId=" + shippingInformationId + ", " : "") +
            (paymentMethodId != null ? "paymentMethodId=" + paymentMethodId + ", " : "") +
            (productsId != null ? "productsId=" + productsId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
