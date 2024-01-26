import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'product',
    data: { pageTitle: 'shopApp.product.home.title' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'shopApp.category.home.title' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'shopApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'order',
    data: { pageTitle: 'shopApp.order.home.title' },
    loadChildren: () => import('./order/order.routes'),
  },
  {
    path: 'cart',
    data: { pageTitle: 'shopApp.cart.home.title' },
    loadChildren: () => import('./cart/cart.routes'),
  },
  {
    path: 'payment-method',
    data: { pageTitle: 'shopApp.paymentMethod.home.title' },
    loadChildren: () => import('./payment-method/payment-method.routes'),
  },
  {
    path: 'shipping-information',
    data: { pageTitle: 'shopApp.shippingInformation.home.title' },
    loadChildren: () => import('./shipping-information/shipping-information.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
