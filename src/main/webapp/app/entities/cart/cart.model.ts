import { ICustomer } from 'app/entities/customer/customer.model';
import { IProduct } from 'app/entities/product/product.model';

export interface ICart {
  id: number | any;
  totalPrice?: number | null;
  productAmount?: number | null;
  customer?: ICustomer | null;
  product?: IProduct | null;
  products?: IProduct[] | null; // addToCart
}

export type NewCart = Omit<ICart, 'id'> & { id: number | null }; // Added number to prevent: "Type 'number' is not assignable to type 'null'"
