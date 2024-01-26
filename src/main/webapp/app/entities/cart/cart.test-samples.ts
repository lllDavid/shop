import { ICart, NewCart } from './cart.model';

export const sampleWithRequiredData: ICart = {
  id: 26152,
};

export const sampleWithPartialData: ICart = {
  id: 16292,
};

export const sampleWithFullData: ICart = {
  id: 31104,
  totalPrice: 15426.54,
  productAmount: 26058.16,
};

export const sampleWithNewData: NewCart = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
