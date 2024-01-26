package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.ShippingInformation} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ShippingInformationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shipping-informations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShippingInformationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter deliveryAddress;

    private StringFilter shippingStatus;

    private LongFilter customerId;

    private LongFilter orderId;

    private Boolean distinct;

    public ShippingInformationCriteria() {}

    public ShippingInformationCriteria(ShippingInformationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.deliveryAddress = other.deliveryAddress == null ? null : other.deliveryAddress.copy();
        this.shippingStatus = other.shippingStatus == null ? null : other.shippingStatus.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ShippingInformationCriteria copy() {
        return new ShippingInformationCriteria(this);
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

    public StringFilter getDeliveryAddress() {
        return deliveryAddress;
    }

    public StringFilter deliveryAddress() {
        if (deliveryAddress == null) {
            deliveryAddress = new StringFilter();
        }
        return deliveryAddress;
    }

    public void setDeliveryAddress(StringFilter deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public StringFilter getShippingStatus() {
        return shippingStatus;
    }

    public StringFilter shippingStatus() {
        if (shippingStatus == null) {
            shippingStatus = new StringFilter();
        }
        return shippingStatus;
    }

    public void setShippingStatus(StringFilter shippingStatus) {
        this.shippingStatus = shippingStatus;
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

    public LongFilter getOrderId() {
        return orderId;
    }

    public LongFilter orderId() {
        if (orderId == null) {
            orderId = new LongFilter();
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
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
        final ShippingInformationCriteria that = (ShippingInformationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(deliveryAddress, that.deliveryAddress) &&
            Objects.equals(shippingStatus, that.shippingStatus) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deliveryAddress, shippingStatus, customerId, orderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShippingInformationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (deliveryAddress != null ? "deliveryAddress=" + deliveryAddress + ", " : "") +
            (shippingStatus != null ? "shippingStatus=" + shippingStatus + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (orderId != null ? "orderId=" + orderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
