import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IShippingInformation } from '../shipping-information.model';
import { ShippingInformationService } from '../service/shipping-information.service';

@Component({
  standalone: true,
  templateUrl: './shipping-information-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ShippingInformationDeleteDialogComponent {
  shippingInformation?: IShippingInformation;

  constructor(
    protected shippingInformationService: ShippingInformationService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shippingInformationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
