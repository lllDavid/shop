import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IShippingInformation } from 'app/entities/shipping-information/shipping-information.model';
import { ShippingInformationService } from 'app/entities/shipping-information/service/shipping-information.service';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/service/payment-method.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IOrder } from '../order.model';
import { OrderService } from '../service/order.service';
import { OrderFormService } from './order-form.service';

import { OrderUpdateComponent } from './order-update.component';

describe('Order Management Update Component', () => {
  let comp: OrderUpdateComponent;
  let fixture: ComponentFixture<OrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderFormService: OrderFormService;
  let orderService: OrderService;
  let shippingInformationService: ShippingInformationService;
  let paymentMethodService: PaymentMethodService;
  let productService: ProductService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrderUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderFormService = TestBed.inject(OrderFormService);
    orderService = TestBed.inject(OrderService);
    shippingInformationService = TestBed.inject(ShippingInformationService);
    paymentMethodService = TestBed.inject(PaymentMethodService);
    productService = TestBed.inject(ProductService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call shippingInformation query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const shippingInformation: IShippingInformation = { id: 22290 };
      order.shippingInformation = shippingInformation;

      const shippingInformationCollection: IShippingInformation[] = [{ id: 28945 }];
      jest.spyOn(shippingInformationService, 'query').mockReturnValue(of(new HttpResponse({ body: shippingInformationCollection })));
      const expectedCollection: IShippingInformation[] = [shippingInformation, ...shippingInformationCollection];
      jest.spyOn(shippingInformationService, 'addShippingInformationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(shippingInformationService.query).toHaveBeenCalled();
      expect(shippingInformationService.addShippingInformationToCollectionIfMissing).toHaveBeenCalledWith(
        shippingInformationCollection,
        shippingInformation,
      );
      expect(comp.shippingInformationsCollection).toEqual(expectedCollection);
    });

    it('Should call PaymentMethod query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const paymentMethod: IPaymentMethod = { id: 15099 };
      order.paymentMethod = paymentMethod;

      const paymentMethodCollection: IPaymentMethod[] = [{ id: 24736 }];
      jest.spyOn(paymentMethodService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentMethodCollection })));
      const additionalPaymentMethods = [paymentMethod];
      const expectedCollection: IPaymentMethod[] = [...additionalPaymentMethods, ...paymentMethodCollection];
      jest.spyOn(paymentMethodService, 'addPaymentMethodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(paymentMethodService.query).toHaveBeenCalled();
      expect(paymentMethodService.addPaymentMethodToCollectionIfMissing).toHaveBeenCalledWith(
        paymentMethodCollection,
        ...additionalPaymentMethods.map(expect.objectContaining),
      );
      expect(comp.paymentMethodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const products: IProduct[] = [{ id: 16677 }];
      order.products = products;

      const productCollection: IProduct[] = [{ id: 7825 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [...products];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining),
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Customer query and add missing value', () => {
      const order: IOrder = { id: 456 };
      const customer: ICustomer = { id: 9161 };
      order.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 2979 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const order: IOrder = { id: 456 };
      const shippingInformation: IShippingInformation = { id: 27487 };
      order.shippingInformation = shippingInformation;
      const paymentMethod: IPaymentMethod = { id: 9545 };
      order.paymentMethod = paymentMethod;
      const products: IProduct = { id: 28099 };
      order.products = [products];
      const customer: ICustomer = { id: 2921 };
      order.customer = customer;

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(comp.shippingInformationsCollection).toContain(shippingInformation);
      expect(comp.paymentMethodsSharedCollection).toContain(paymentMethod);
      expect(comp.productsSharedCollection).toContain(products);
      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.order).toEqual(order);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderFormService, 'getOrder').mockReturnValue(order);
      jest.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: order }));
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderService.update).toHaveBeenCalledWith(expect.objectContaining(order));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderFormService, 'getOrder').mockReturnValue({ id: null });
      jest.spyOn(orderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: order }));
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(orderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 123 };
      jest.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareShippingInformation', () => {
      it('Should forward to shippingInformationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(shippingInformationService, 'compareShippingInformation');
        comp.compareShippingInformation(entity, entity2);
        expect(shippingInformationService.compareShippingInformation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePaymentMethod', () => {
      it('Should forward to paymentMethodService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(paymentMethodService, 'comparePaymentMethod');
        comp.comparePaymentMethod(entity, entity2);
        expect(paymentMethodService.comparePaymentMethod).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
