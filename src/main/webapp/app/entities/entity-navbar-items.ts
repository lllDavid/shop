import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Product',
    route: '/product',
    translationKey: 'global.menu.entities.product',
  },
  {
    name: 'Category',
    route: '/category',
    translationKey: 'global.menu.entities.category',
  },
  {
    name: 'Customer',
    route: '/customer',
    translationKey: 'global.menu.entities.customer',
  },
  {
    name: 'Order',
    route: '/order',
    translationKey: 'global.menu.entities.order',
  },
  {
    name: 'Cart',
    route: '/cart',
    translationKey: 'global.menu.entities.cart',
  },
  {
    name: 'PaymentMethod',
    route: '/payment-method',
    translationKey: 'global.menu.entities.paymentMethod',
  },
  {
    name: 'ShippingInformation',
    route: '/shipping-information',
    translationKey: 'global.menu.entities.shippingInformation',
  },
];
