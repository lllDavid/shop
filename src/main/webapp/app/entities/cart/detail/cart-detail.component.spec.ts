import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CartDetailComponent } from './cart-detail.component';

describe('Cart Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CartDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CartDetailComponent,
              resolve: { cart: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CartDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load cart on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CartDetailComponent);

      // THEN
      expect(instance.cart).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
