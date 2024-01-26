import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PaymentMethodService } from '../service/payment-method.service';

import { PaymentMethodComponent } from './payment-method.component';

describe('PaymentMethod Management Component', () => {
  let comp: PaymentMethodComponent;
  let fixture: ComponentFixture<PaymentMethodComponent>;
  let service: PaymentMethodService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'payment-method', component: PaymentMethodComponent }]),
        HttpClientTestingModule,
        PaymentMethodComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
                'filter[someId.in]': 'dc4279ea-cfb9-11ec-9d64-0242ac120002',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(PaymentMethodComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaymentMethodComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PaymentMethodService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.paymentMethods?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to paymentMethodService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getPaymentMethodIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPaymentMethodIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
