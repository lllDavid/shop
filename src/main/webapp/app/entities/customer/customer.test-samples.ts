import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 1363,
};

export const sampleWithPartialData: ICustomer = {
  id: 736,
  firstName: 'Lorine',
};

export const sampleWithFullData: ICustomer = {
  id: 7629,
  lastName: 'Kemmer',
  firstName: 'Austen',
  address: 'ugh produce for',
  email: 'Glen67@hotmail.com',
  password: 'jumpy ouch whenever',
};

export const sampleWithNewData: NewCustomer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
