import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortDirective, SortByDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import { SortService } from 'app/shared/sort/sort.service';
import { CartService } from 'app/entities/cart/service/cart.service';
import { IProduct } from '../product.model';
import { EntityArrayResponseType, ProductService } from '../service/product.service';
import { ProductDeleteDialogComponent } from '../delete/product-delete-dialog.component';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';

@Component({
  standalone: true,
  selector: 'jhi-product',
  templateUrl: './product.component.html',
  styleUrl: './product.component.css',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    HasAnyAuthorityDirective,
  ],
})
export class ProductComponent implements OnInit {
  products?: IProduct[];
  userInput: string = ''; // searchProducts()
  autocompleteSuggestions: string[] = []; // getAutocompleteSuggestions()
  customQuantity?: number | null = null; // addToCart()

  isLoading = false;

  predicate = 'id';
  ascending = true;

  constructor(
    protected productService: ProductService,
    private cartService: CartService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
  ) {}

  trackId = (_index: number, item: IProduct): number => this.productService.getProductIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  searchProductsIsActivated = false;

  searchProducts(): void {
    this.productService.findProductByName(this.userInput).subscribe({
      next: response => {
        this.products = response.body ? response.body : undefined;
      },
    });
    this.searchProductsIsActivated = true;

    console.log(this.searchProductsIsActivated);
  }

  // getAutocompleteSuggestions()
  getAutocompleteSuggestions(): void {
    if (this.userInput.trim() !== '') {
      this.productService.findAutocompleteSuggestions(this.userInput).subscribe(response => {
        this.autocompleteSuggestions = response;
      });
    } else {
      this.autocompleteSuggestions = [];
    }
  }
  // autocompleteOnInputKeyUp()
  autocompleteOnInputKeyUp(): void {
    this.getAutocompleteSuggestions();
  }

  //selectAutocompleteSuggestion()
  selectAutocompleteSuggestion(suggestion: string): void {
    this.userInput = suggestion;
    this.autocompleteSuggestions = [];
    this.searchProducts();
  }

  //addToCart()
  addToCart(product: IProduct): void {
    console.log(product.quantity);
    const quantity = product.quantity === 'Custom' ? this.customQuantity || 1 : parseInt(product.quantity, 10) || 1;
    this.cartService.addToCart({ ...product, quantity });
    product.quantity = '1';
  }

  onLoadSetQuantity(product: IProduct): void {
    product.quantity = '1';
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(product: IProduct): void {
    const modalRef = this.modalService.open(ProductDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.product = product;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations()),
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        // added loop to set quantity value
        if (res.body != null)
          for (const value of Object.values(res.body)) {
            value.quantity = '1';

            if (this.searchProductsIsActivated == true) {
              value.quantity = '0';
              this.searchProductsIsActivated = false;
              if (value != null) {
                console.log(this.searchProductsIsActivated);
              }
            }
            console.log(value);
          }

        this.onResponseSuccess(res);
        console.log(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.predicate, this.ascending)),
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.products = this.refineData(dataFromBody);
  }

  protected refineData(data: IProduct[]): IProduct[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? 1 : -1));
  }

  protected fillComponentAttributesFromResponseBody(data: IProduct[] | null): IProduct[] {
    return data ?? [];
  }

  protected queryBackend(predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.productService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
