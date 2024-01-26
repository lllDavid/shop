import dayjs from 'dayjs/esm';
import { IShippingInformation } from 'app/entities/shipping-information/shipping-information.model';
import { IPaymentMethod } from 'app/entities/payment-method/payment-method.model';
import { IProduct } from 'app/entities/product/product.model';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IOrder {
  id: number;
  date?: dayjs.Dayjs | null;
  totalPrice?: number | null;
  shippingInformation?: IShippingInformation | null;
  paymentMethod?: IPaymentMethod | null;
  products?: IProduct[] | null;
  customer?: ICustomer | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
