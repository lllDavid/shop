import { IOrder } from 'app/entities/order/order.model';
import { IProduct } from 'app/entities/product/product.model';
import { ICart } from 'app/entities/cart/cart.model';
import { IShippingInformation } from 'app/entities/shipping-information/shipping-information.model';

export interface ICustomer {
  id: number;
  lastName?: string | null;
  firstName?: string | null;
  address?: string | null;
  email?: string | null;
  password?: string | null;
  orders?: IOrder[] | null;
  favoriteProducts?: IProduct[] | null;
  carts?: ICart[] | null;
  shippingInformations?: IShippingInformation[] | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
