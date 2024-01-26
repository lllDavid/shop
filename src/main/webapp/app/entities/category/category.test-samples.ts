import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 6350,
};

export const sampleWithPartialData: ICategory = {
  id: 19698,
  description: 'pine',
};

export const sampleWithFullData: ICategory = {
  id: 10796,
  name: 'distinct',
  description: 'frayed boohoo meanwhile',
};

export const sampleWithNewData: NewCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
