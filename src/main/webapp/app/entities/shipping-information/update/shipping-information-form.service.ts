import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IShippingInformation, NewShippingInformation } from '../shipping-information.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShippingInformation for edit and NewShippingInformationFormGroupInput for create.
 */
type ShippingInformationFormGroupInput = IShippingInformation | PartialWithRequiredKeyOf<NewShippingInformation>;

type ShippingInformationFormDefaults = Pick<NewShippingInformation, 'id'>;

type ShippingInformationFormGroupContent = {
  id: FormControl<IShippingInformation['id'] | NewShippingInformation['id']>;
  deliveryAddress: FormControl<IShippingInformation['deliveryAddress']>;
  shippingStatus: FormControl<IShippingInformation['shippingStatus']>;
  customer: FormControl<IShippingInformation['customer']>;
};

export type ShippingInformationFormGroup = FormGroup<ShippingInformationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ShippingInformationFormService {
  createShippingInformationFormGroup(shippingInformation: ShippingInformationFormGroupInput = { id: null }): ShippingInformationFormGroup {
    const shippingInformationRawValue = {
      ...this.getFormDefaults(),
      ...shippingInformation,
    };
    return new FormGroup<ShippingInformationFormGroupContent>({
      id: new FormControl(
        { value: shippingInformationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      deliveryAddress: new FormControl(shippingInformationRawValue.deliveryAddress),
      shippingStatus: new FormControl(shippingInformationRawValue.shippingStatus),
      customer: new FormControl(shippingInformationRawValue.customer),
    });
  }

  getShippingInformation(form: ShippingInformationFormGroup): IShippingInformation | NewShippingInformation {
    return form.getRawValue() as IShippingInformation | NewShippingInformation;
  }

  resetForm(form: ShippingInformationFormGroup, shippingInformation: ShippingInformationFormGroupInput): void {
    const shippingInformationRawValue = { ...this.getFormDefaults(), ...shippingInformation };
    form.reset(
      {
        ...shippingInformationRawValue,
        id: { value: shippingInformationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ShippingInformationFormDefaults {
    return {
      id: null,
    };
  }
}
