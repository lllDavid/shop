import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ShippingInformationService } from '../service/shipping-information.service';
import { IShippingInformation } from '../shipping-information.model';
import { ShippingInformationFormService } from './shipping-information-form.service';

import { ShippingInformationUpdateComponent } from './shipping-information-update.component';

describe('ShippingInformation Management Update Component', () => {
  let comp: ShippingInformationUpdateComponent;
  let fixture: ComponentFixture<ShippingInformationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let shippingInformationFormService: ShippingInformationFormService;
  let shippingInformationService: ShippingInformationService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ShippingInformationUpdateComponent],
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
      .overrideTemplate(ShippingInformationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShippingInformationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shippingInformationFormService = TestBed.inject(ShippingInformationFormService);
    shippingInformationService = TestBed.inject(ShippingInformationService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const shippingInformation: IShippingInformation = { id: 456 };
      const customer: ICustomer = { id: 16992 };
      shippingInformation.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 21146 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shippingInformation });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const shippingInformation: IShippingInformation = { id: 456 };
      const customer: ICustomer = { id: 26830 };
      shippingInformation.customer = customer;

      activatedRoute.data = of({ shippingInformation });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.shippingInformation).toEqual(shippingInformation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShippingInformation>>();
      const shippingInformation = { id: 123 };
      jest.spyOn(shippingInformationFormService, 'getShippingInformation').mockReturnValue(shippingInformation);
      jest.spyOn(shippingInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shippingInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shippingInformation }));
      saveSubject.complete();

      // THEN
      expect(shippingInformationFormService.getShippingInformation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shippingInformationService.update).toHaveBeenCalledWith(expect.objectContaining(shippingInformation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShippingInformation>>();
      const shippingInformation = { id: 123 };
      jest.spyOn(shippingInformationFormService, 'getShippingInformation').mockReturnValue({ id: null });
      jest.spyOn(shippingInformationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shippingInformation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shippingInformation }));
      saveSubject.complete();

      // THEN
      expect(shippingInformationFormService.getShippingInformation).toHaveBeenCalled();
      expect(shippingInformationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShippingInformation>>();
      const shippingInformation = { id: 123 };
      jest.spyOn(shippingInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shippingInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shippingInformationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
