import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICart } from '../cart.model';
import { CartService } from '../service/cart.service';

@Component({
  standalone: true,
  templateUrl: './cart-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CartDeleteDialogComponent {
  cart?: ICart;

  constructor(
    protected cartService: CartService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cartService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
