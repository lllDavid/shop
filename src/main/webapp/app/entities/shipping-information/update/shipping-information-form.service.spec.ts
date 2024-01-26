import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../shipping-information.test-samples';

import { ShippingInformationFormService } from './shipping-information-form.service';

describe('ShippingInformation Form Service', () => {
  let service: ShippingInformationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShippingInformationFormService);
  });

  describe('Service methods', () => {
    describe('createShippingInformationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShippingInformationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            deliveryAddress: expect.any(Object),
            shippingStatus: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });

      it('passing IShippingInformation should create a new form with FormGroup', () => {
        const formGroup = service.createShippingInformationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            deliveryAddress: expect.any(Object),
            shippingStatus: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });
    });

    describe('getShippingInformation', () => {
      it('should return NewShippingInformation for default ShippingInformation initial value', () => {
        const formGroup = service.createShippingInformationFormGroup(sampleWithNewData);

        const shippingInformation = service.getShippingInformation(formGroup) as any;

        expect(shippingInformation).toMatchObject(sampleWithNewData);
      });

      it('should return NewShippingInformation for empty ShippingInformation initial value', () => {
        const formGroup = service.createShippingInformationFormGroup();

        const shippingInformation = service.getShippingInformation(formGroup) as any;

        expect(shippingInformation).toMatchObject({});
      });

      it('should return IShippingInformation', () => {
        const formGroup = service.createShippingInformationFormGroup(sampleWithRequiredData);

        const shippingInformation = service.getShippingInformation(formGroup) as any;

        expect(shippingInformation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShippingInformation should not enable id FormControl', () => {
        const formGroup = service.createShippingInformationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShippingInformation should disable id FormControl', () => {
        const formGroup = service.createShippingInformationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
