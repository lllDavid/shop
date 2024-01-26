import { ICustomer } from 'app/entities/customer/customer.model';
import { IOrder } from 'app/entities/order/order.model';

export interface IShippingInformation {
  id: number;
  deliveryAddress?: string | null;
  shippingStatus?: string | null;
  customer?: ICustomer | null;
  order?: IOrder | null;
}

export type NewShippingInformation = Omit<IShippingInformation, 'id'> & { id: null };
