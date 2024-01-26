import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PaymentMethodDetailComponent } from './payment-method-detail.component';

describe('PaymentMethod Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaymentMethodDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PaymentMethodDetailComponent,
              resolve: { paymentMethod: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PaymentMethodDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load paymentMethod on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PaymentMethodDetailComponent);

      // THEN
      expect(instance.paymentMethod).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
