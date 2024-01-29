import { ICustomer } from 'app/entities/customer/customer.model';
import { IProduct } from 'app/entities/product/product.model';

export interface ICart {
  id: number;
  totalPrice?: number | null;
  productAmount?: number | null;
  customer?: ICustomer | null;
  product?: IProduct | null;
}

export type NewCart = Omit<ICart, 'id'> & { id: null }; 
