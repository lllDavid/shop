import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentMethod } from '../payment-method.model';
import { PaymentMethodService } from '../service/payment-method.service';

export const paymentMethodResolve = (route: ActivatedRouteSnapshot): Observable<null | IPaymentMethod> => {
  const id = route.params['id'];
  if (id) {
    return inject(PaymentMethodService)
      .find(id)
      .pipe(
        mergeMap((paymentMethod: HttpResponse<IPaymentMethod>) => {
          if (paymentMethod.body) {
            return of(paymentMethod.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default paymentMethodResolve;
