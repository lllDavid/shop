import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ShippingInformationService } from '../service/shipping-information.service';

import { ShippingInformationComponent } from './shipping-information.component';

describe('ShippingInformation Management Component', () => {
  let comp: ShippingInformationComponent;
  let fixture: ComponentFixture<ShippingInformationComponent>;
  let service: ShippingInformationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'shipping-information', component: ShippingInformationComponent }]),
        HttpClientTestingModule,
        ShippingInformationComponent,
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
      .overrideTemplate(ShippingInformationComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShippingInformationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ShippingInformationService);

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
    expect(comp.shippingInformations?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to shippingInformationService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getShippingInformationIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getShippingInformationIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
