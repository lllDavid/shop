import dayjs from 'dayjs/esm';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 830,
};

export const sampleWithPartialData: IOrder = {
  id: 1222,
  totalPrice: 26769.37,
};

export const sampleWithFullData: IOrder = {
  id: 30252,
  date: dayjs('2019-12-22T05:26'),
  totalPrice: 5620.2,
};

export const sampleWithNewData: NewOrder = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
