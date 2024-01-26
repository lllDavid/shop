import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IShippingInformation, NewShippingInformation } from '../shipping-information.model';

export type PartialUpdateShippingInformation = Partial<IShippingInformation> & Pick<IShippingInformation, 'id'>;

export type EntityResponseType = HttpResponse<IShippingInformation>;
export type EntityArrayResponseType = HttpResponse<IShippingInformation[]>;

@Injectable({ providedIn: 'root' })
export class ShippingInformationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/shipping-informations');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(shippingInformation: NewShippingInformation): Observable<EntityResponseType> {
    return this.http.post<IShippingInformation>(this.resourceUrl, shippingInformation, { observe: 'response' });
  }

  update(shippingInformation: IShippingInformation): Observable<EntityResponseType> {
    return this.http.put<IShippingInformation>(
      `${this.resourceUrl}/${this.getShippingInformationIdentifier(shippingInformation)}`,
      shippingInformation,
      { observe: 'response' },
    );
  }

  partialUpdate(shippingInformation: PartialUpdateShippingInformation): Observable<EntityResponseType> {
    return this.http.patch<IShippingInformation>(
      `${this.resourceUrl}/${this.getShippingInformationIdentifier(shippingInformation)}`,
      shippingInformation,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShippingInformation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShippingInformation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getShippingInformationIdentifier(shippingInformation: Pick<IShippingInformation, 'id'>): number {
    return shippingInformation.id;
  }

  compareShippingInformation(o1: Pick<IShippingInformation, 'id'> | null, o2: Pick<IShippingInformation, 'id'> | null): boolean {
    return o1 && o2 ? this.getShippingInformationIdentifier(o1) === this.getShippingInformationIdentifier(o2) : o1 === o2;
  }

  addShippingInformationToCollectionIfMissing<Type extends Pick<IShippingInformation, 'id'>>(
    shippingInformationCollection: Type[],
    ...shippingInformationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shippingInformations: Type[] = shippingInformationsToCheck.filter(isPresent);
    if (shippingInformations.length > 0) {
      const shippingInformationCollectionIdentifiers = shippingInformationCollection.map(
        shippingInformationItem => this.getShippingInformationIdentifier(shippingInformationItem)!,
      );
      const shippingInformationsToAdd = shippingInformations.filter(shippingInformationItem => {
        const shippingInformationIdentifier = this.getShippingInformationIdentifier(shippingInformationItem);
        if (shippingInformationCollectionIdentifiers.includes(shippingInformationIdentifier)) {
          return false;
        }
        shippingInformationCollectionIdentifiers.push(shippingInformationIdentifier);
        return true;
      });
      return [...shippingInformationsToAdd, ...shippingInformationCollection];
    }
    return shippingInformationCollection;
  }
}
