import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CartComponent } from './list/cart.component';
import { CartDetailComponent } from './detail/cart-detail.component';
import { CartUpdateComponent } from './update/cart-update.component';
import CartResolve from './route/cart-routing-resolve.service';

const cartRoute: Routes = [
  {
    path: '',
    component: CartComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CartDetailComponent,
    resolve: {
      cart: CartResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CartUpdateComponent,
    resolve: {
      cart: CartResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CartUpdateComponent,
    resolve: {
      cart: CartResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cartRoute;
