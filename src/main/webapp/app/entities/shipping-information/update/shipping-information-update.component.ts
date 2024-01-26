import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IShippingInformation } from '../shipping-information.model';
import { ShippingInformationService } from '../service/shipping-information.service';
import { ShippingInformationFormService, ShippingInformationFormGroup } from './shipping-information-form.service';

@Component({
  standalone: true,
  selector: 'jhi-shipping-information-update',
  templateUrl: './shipping-information-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ShippingInformationUpdateComponent implements OnInit {
  isSaving = false;
  shippingInformation: IShippingInformation | null = null;

  customersSharedCollection: ICustomer[] = [];

  editForm: ShippingInformationFormGroup = this.shippingInformationFormService.createShippingInformationFormGroup();

  constructor(
    protected shippingInformationService: ShippingInformationService,
    protected shippingInformationFormService: ShippingInformationFormService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shippingInformation }) => {
      this.shippingInformation = shippingInformation;
      if (shippingInformation) {
        this.updateForm(shippingInformation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shippingInformation = this.shippingInformationFormService.getShippingInformation(this.editForm);
    if (shippingInformation.id !== null) {
      this.subscribeToSaveResponse(this.shippingInformationService.update(shippingInformation));
    } else {
      this.subscribeToSaveResponse(this.shippingInformationService.create(shippingInformation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShippingInformation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(shippingInformation: IShippingInformation): void {
    this.shippingInformation = shippingInformation;
    this.shippingInformationFormService.resetForm(this.editForm, shippingInformation);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      shippingInformation.customer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.shippingInformation?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }
}
