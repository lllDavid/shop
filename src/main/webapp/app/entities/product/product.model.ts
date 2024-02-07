import { ICategory } from 'app/entities/category/category.model';
import { IOrder } from 'app/entities/order/order.model';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IProduct {
  id: number;
  name?: string | null;
  description?: string | null;
  price?: number | null;
  inStock?: number | null;
  image?: string | null;
  imageContentType?: string | null;
  category?: ICategory | null;
  orders?: IOrder[] | null;
  favoritedByCustomers?: ICustomer[] | null;
  quantity?: number | any; // Added for addToCart()
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
