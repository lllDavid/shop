import { IShippingInformation, NewShippingInformation } from './shipping-information.model';

export const sampleWithRequiredData: IShippingInformation = {
  id: 2436,
};

export const sampleWithPartialData: IShippingInformation = {
  id: 27637,
};

export const sampleWithFullData: IShippingInformation = {
  id: 28479,
  deliveryAddress: 'innocently',
  shippingStatus: 'exactly hastily what',
};

export const sampleWithNewData: NewShippingInformation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
