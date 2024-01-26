import { IPaymentMethod, NewPaymentMethod } from './payment-method.model';

export const sampleWithRequiredData: IPaymentMethod = {
  id: 30145,
};

export const sampleWithPartialData: IPaymentMethod = {
  id: 18904,
  description: 'certainty',
};

export const sampleWithFullData: IPaymentMethod = {
  id: 22691,
  name: 'suckle notwithstanding',
  description: 'solicitation foolhardy',
};

export const sampleWithNewData: NewPaymentMethod = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
