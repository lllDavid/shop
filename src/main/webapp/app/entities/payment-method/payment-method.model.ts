import { IOrder } from 'app/entities/order/order.model';

export interface IPaymentMethod {
  id: number;
  name?: string | null;
  description?: string | null;
  orders?: IOrder[] | null;
}

export type NewPaymentMethod = Omit<IPaymentMethod, 'id'> & { id: null };
