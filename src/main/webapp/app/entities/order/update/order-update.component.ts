import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IShippingInformation } from 'app/entities/shipping-information/shipping-information.model';
import { ShippingInformationService } from 'app/entities/shipping-information/service/shipping-information.service';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/service/payment-method.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { OrderService } from '../service/order.service';
import { IOrder } from '../order.model';
import { OrderFormService, OrderFormGroup } from './order-form.service';

@Component({
  standalone: true,
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrderUpdateComponent implements OnInit {
  isSaving = false;
  order: IOrder | null = null;

  shippingInformationsCollection: IShippingInformation[] = [];
  paymentMethodsSharedCollection: IPaymentMethod[] = [];
  productsSharedCollection: IProduct[] = [];
  customersSharedCollection: ICustomer[] = [];

  editForm: OrderFormGroup = this.orderFormService.createOrderFormGroup();

  constructor(
    protected orderService: OrderService,
    protected orderFormService: OrderFormService,
    protected shippingInformationService: ShippingInformationService,
    protected paymentMethodService: PaymentMethodService,
    protected productService: ProductService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareShippingInformation = (o1: IShippingInformation | null, o2: IShippingInformation | null): boolean =>
    this.shippingInformationService.compareShippingInformation(o1, o2);

  comparePaymentMethod = (o1: IPaymentMethod | null, o2: IPaymentMethod | null): boolean =>
    this.paymentMethodService.comparePaymentMethod(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      this.order = order;
      if (order) {
        this.updateForm(order);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const order = this.orderFormService.getOrder(this.editForm);
    if (order.id !== null) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>): void {
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

  protected updateForm(order: IOrder): void {
    this.order = order;
    this.orderFormService.resetForm(this.editForm, order);

    this.shippingInformationsCollection = this.shippingInformationService.addShippingInformationToCollectionIfMissing<IShippingInformation>(
      this.shippingInformationsCollection,
      order.shippingInformation,
    );
    this.paymentMethodsSharedCollection = this.paymentMethodService.addPaymentMethodToCollectionIfMissing<IPaymentMethod>(
      this.paymentMethodsSharedCollection,
      order.paymentMethod,
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      ...(order.products ?? []),
    );
    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      order.customer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.shippingInformationService
      .query({ 'orderId.specified': 'false' })
      .pipe(map((res: HttpResponse<IShippingInformation[]>) => res.body ?? []))
      .pipe(
        map((shippingInformations: IShippingInformation[]) =>
          this.shippingInformationService.addShippingInformationToCollectionIfMissing<IShippingInformation>(
            shippingInformations,
            this.order?.shippingInformation,
          ),
        ),
      )
      .subscribe((shippingInformations: IShippingInformation[]) => (this.shippingInformationsCollection = shippingInformations));

    this.paymentMethodService
      .query()
      .pipe(map((res: HttpResponse<IPaymentMethod[]>) => res.body ?? []))
      .pipe(
        map((paymentMethods: IPaymentMethod[]) =>
          this.paymentMethodService.addPaymentMethodToCollectionIfMissing<IPaymentMethod>(paymentMethods, this.order?.paymentMethod),
        ),
      )
      .subscribe((paymentMethods: IPaymentMethod[]) => (this.paymentMethodsSharedCollection = paymentMethods));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing<IProduct>(products, ...(this.order?.products ?? [])),
        ),
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) => this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.order?.customer)),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }
}
