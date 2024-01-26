import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 25692,
};

export const sampleWithPartialData: IProduct = {
  id: 14424,
  price: 11928.58,
  inStock: 32183,
};

export const sampleWithFullData: IProduct = {
  id: 1644,
  name: 'interview intently revolutionise',
  description: 'huzzah clumsy bandwidth',
  price: 13473.79,
  inStock: 11068,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithNewData: NewProduct = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
