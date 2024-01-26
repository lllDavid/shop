import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShippingInformation } from '../shipping-information.model';
import { ShippingInformationService } from '../service/shipping-information.service';

export const shippingInformationResolve = (route: ActivatedRouteSnapshot): Observable<null | IShippingInformation> => {
  const id = route.params['id'];
  if (id) {
    return inject(ShippingInformationService)
      .find(id)
      .pipe(
        mergeMap((shippingInformation: HttpResponse<IShippingInformation>) => {
          if (shippingInformation.body) {
            return of(shippingInformation.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default shippingInformationResolve;
