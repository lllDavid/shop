import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PaymentMethodComponent } from './list/payment-method.component';
import { PaymentMethodDetailComponent } from './detail/payment-method-detail.component';
import { PaymentMethodUpdateComponent } from './update/payment-method-update.component';
import PaymentMethodResolve from './route/payment-method-routing-resolve.service';

const paymentMethodRoute: Routes = [
  {
    path: '',
    component: PaymentMethodComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentMethodDetailComponent,
    resolve: {
      paymentMethod: PaymentMethodResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentMethodUpdateComponent,
    resolve: {
      paymentMethod: PaymentMethodResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentMethodUpdateComponent,
    resolve: {
      paymentMethod: PaymentMethodResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default paymentMethodRoute;
