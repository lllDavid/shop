import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { CartService } from '../service/cart.service';
import { ICart } from '../cart.model';
import { CartFormService, CartFormGroup } from './cart-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cart-update',
  templateUrl: './cart-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CartUpdateComponent implements OnInit {
  isSaving = false;
  cart: ICart | null = null;

  customersSharedCollection: ICustomer[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm: CartFormGroup = this.cartFormService.createCartFormGroup();

  constructor(
    protected cartService: CartService,
    protected cartFormService: CartFormService,
    protected customerService: CustomerService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
  ) { }

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cart }) => {
      this.cart = cart;
      if (cart) {
        this.updateForm(cart);
      }
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cart = this.cartFormService.getCart(this.editForm);
    if (cart.id !== null) {
      this.subscribeToSaveResponse(this.cartService.update(cart));
    } else {
      this.subscribeToSaveResponse(this.cartService.create(cart));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICart>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cart: ICart): void {
    this.cart = cart;
    this.cartFormService.resetForm(this.editForm, cart);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      cart.customer,
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      cart.product,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) => this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.cart?.customer)),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.cart?.product)))
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
