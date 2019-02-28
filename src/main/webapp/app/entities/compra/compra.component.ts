import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICompra } from 'app/shared/model/compra.model';
import { AccountService } from 'app/core';
import { CompraService } from './compra.service';

@Component({
    selector: 'jhi-compra',
    templateUrl: './compra.component.html'
})
export class CompraComponent implements OnInit, OnDestroy {
    compras: ICompra[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected compraService: CompraService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.compraService
            .query()
            .pipe(
                filter((res: HttpResponse<ICompra[]>) => res.ok),
                map((res: HttpResponse<ICompra[]>) => res.body)
            )
            .subscribe(
                (res: ICompra[]) => {
                    this.compras = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCompras();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICompra) {
        return item.id;
    }

    registerChangeInCompras() {
        this.eventSubscriber = this.eventManager.subscribe('compraListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
