package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Customer} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter lastName;

    private StringFilter firstName;

    private StringFilter address;

    private StringFilter email;

    private StringFilter password;

    private LongFilter ordersId;

    private LongFilter favoriteProductsId;

    private LongFilter cartsId;

    private LongFilter shippingInformationId;

    private Boolean distinct;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.ordersId = other.ordersId == null ? null : other.ordersId.copy();
        this.favoriteProductsId = other.favoriteProductsId == null ? null : other.favoriteProductsId.copy();
        this.cartsId = other.cartsId == null ? null : other.cartsId.copy();
        this.shippingInformationId = other.shippingInformationId == null ? null : other.shippingInformationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
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

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPassword() {
        return password;
    }

    public StringFilter password() {
        if (password == null) {
            password = new StringFilter();
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
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

    public LongFilter getFavoriteProductsId() {
        return favoriteProductsId;
    }

    public LongFilter favoriteProductsId() {
        if (favoriteProductsId == null) {
            favoriteProductsId = new LongFilter();
        }
        return favoriteProductsId;
    }

    public void setFavoriteProductsId(LongFilter favoriteProductsId) {
        this.favoriteProductsId = favoriteProductsId;
    }

    public LongFilter getCartsId() {
        return cartsId;
    }

    public LongFilter cartsId() {
        if (cartsId == null) {
            cartsId = new LongFilter();
        }
        return cartsId;
    }

    public void setCartsId(LongFilter cartsId) {
        this.cartsId = cartsId;
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
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(address, that.address) &&
            Objects.equals(email, that.email) &&
            Objects.equals(password, that.password) &&
            Objects.equals(ordersId, that.ordersId) &&
            Objects.equals(favoriteProductsId, that.favoriteProductsId) &&
            Objects.equals(cartsId, that.cartsId) &&
            Objects.equals(shippingInformationId, that.shippingInformationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            lastName,
            firstName,
            address,
            email,
            password,
            ordersId,
            favoriteProductsId,
            cartsId,
            shippingInformationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (password != null ? "password=" + password + ", " : "") +
            (ordersId != null ? "ordersId=" + ordersId + ", " : "") +
            (favoriteProductsId != null ? "favoriteProductsId=" + favoriteProductsId + ", " : "") +
            (cartsId != null ? "cartsId=" + cartsId + ", " : "") +
            (shippingInformationId != null ? "shippingInformationId=" + shippingInformationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
