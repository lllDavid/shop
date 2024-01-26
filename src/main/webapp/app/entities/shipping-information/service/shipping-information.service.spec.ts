import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IShippingInformation } from '../shipping-information.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../shipping-information.test-samples';

import { ShippingInformationService } from './shipping-information.service';

const requireRestSample: IShippingInformation = {
  ...sampleWithRequiredData,
};

describe('ShippingInformation Service', () => {
  let service: ShippingInformationService;
  let httpMock: HttpTestingController;
  let expectedResult: IShippingInformation | IShippingInformation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ShippingInformationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ShippingInformation', () => {
      const shippingInformation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shippingInformation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShippingInformation', () => {
      const shippingInformation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shippingInformation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShippingInformation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShippingInformation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ShippingInformation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addShippingInformationToCollectionIfMissing', () => {
      it('should add a ShippingInformation to an empty array', () => {
        const shippingInformation: IShippingInformation = sampleWithRequiredData;
        expectedResult = service.addShippingInformationToCollectionIfMissing([], shippingInformation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shippingInformation);
      });

      it('should not add a ShippingInformation to an array that contains it', () => {
        const shippingInformation: IShippingInformation = sampleWithRequiredData;
        const shippingInformationCollection: IShippingInformation[] = [
          {
            ...shippingInformation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShippingInformationToCollectionIfMissing(shippingInformationCollection, shippingInformation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShippingInformation to an array that doesn't contain it", () => {
        const shippingInformation: IShippingInformation = sampleWithRequiredData;
        const shippingInformationCollection: IShippingInformation[] = [sampleWithPartialData];
        expectedResult = service.addShippingInformationToCollectionIfMissing(shippingInformationCollection, shippingInformation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shippingInformation);
      });

      it('should add only unique ShippingInformation to an array', () => {
        const shippingInformationArray: IShippingInformation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shippingInformationCollection: IShippingInformation[] = [sampleWithRequiredData];
        expectedResult = service.addShippingInformationToCollectionIfMissing(shippingInformationCollection, ...shippingInformationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shippingInformation: IShippingInformation = sampleWithRequiredData;
        const shippingInformation2: IShippingInformation = sampleWithPartialData;
        expectedResult = service.addShippingInformationToCollectionIfMissing([], shippingInformation, shippingInformation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shippingInformation);
        expect(expectedResult).toContain(shippingInformation2);
      });

      it('should accept null and undefined values', () => {
        const shippingInformation: IShippingInformation = sampleWithRequiredData;
        expectedResult = service.addShippingInformationToCollectionIfMissing([], null, shippingInformation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shippingInformation);
      });

      it('should return initial array if no ShippingInformation is added', () => {
        const shippingInformationCollection: IShippingInformation[] = [sampleWithRequiredData];
        expectedResult = service.addShippingInformationToCollectionIfMissing(shippingInformationCollection, undefined, null);
        expect(expectedResult).toEqual(shippingInformationCollection);
      });
    });

    describe('compareShippingInformation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShippingInformation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareShippingInformation(entity1, entity2);
        const compareResult2 = service.compareShippingInformation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareShippingInformation(entity1, entity2);
        const compareResult2 = service.compareShippingInformation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareShippingInformation(entity1, entity2);
        const compareResult2 = service.compareShippingInformation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
