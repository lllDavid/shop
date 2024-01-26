import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ShippingInformationDetailComponent } from './shipping-information-detail.component';

describe('ShippingInformation Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShippingInformationDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ShippingInformationDetailComponent,
              resolve: { shippingInformation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ShippingInformationDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load shippingInformation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ShippingInformationDetailComponent);

      // THEN
      expect(instance.shippingInformation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
