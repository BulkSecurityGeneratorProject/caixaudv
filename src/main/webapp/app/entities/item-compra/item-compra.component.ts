import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IItemCompra } from 'app/shared/model/item-compra.model';
import { AccountService } from 'app/core';
import { ItemCompraService } from './item-compra.service';

@Component({
    selector: 'jhi-item-compra',
    templateUrl: './item-compra.component.html'
})
export class ItemCompraComponent implements OnInit, OnDestroy {
    itemCompras: IItemCompra[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected itemCompraService: ItemCompraService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.itemCompraService
            .query()
            .pipe(
                filter((res: HttpResponse<IItemCompra[]>) => res.ok),
                map((res: HttpResponse<IItemCompra[]>) => res.body)
            )
            .subscribe(
                (res: IItemCompra[]) => {
                    this.itemCompras = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInItemCompras();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IItemCompra) {
        return item.id;
    }

    registerChangeInItemCompras() {
        this.eventSubscriber = this.eventManager.subscribe('itemCompraListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
