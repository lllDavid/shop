import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ShippingInformationComponent } from './list/shipping-information.component';
import { ShippingInformationDetailComponent } from './detail/shipping-information-detail.component';
import { ShippingInformationUpdateComponent } from './update/shipping-information-update.component';
import ShippingInformationResolve from './route/shipping-information-routing-resolve.service';

const shippingInformationRoute: Routes = [
  {
    path: '',
    component: ShippingInformationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ShippingInformationDetailComponent,
    resolve: {
      shippingInformation: ShippingInformationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ShippingInformationUpdateComponent,
    resolve: {
      shippingInformation: ShippingInformationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ShippingInformationUpdateComponent,
    resolve: {
      shippingInformation: ShippingInformationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default shippingInformationRoute;
