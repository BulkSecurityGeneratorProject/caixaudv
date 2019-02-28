import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IConta } from 'app/shared/model/conta.model';
import { AccountService } from 'app/core';
import { ContaService } from './conta.service';

@Component({
    selector: 'jhi-conta',
    templateUrl: './conta.component.html'
})
export class ContaComponent implements OnInit, OnDestroy {
    contas: IConta[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected contaService: ContaService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.contaService
            .query()
            .pipe(
                filter((res: HttpResponse<IConta[]>) => res.ok),
                map((res: HttpResponse<IConta[]>) => res.body)
            )
            .subscribe(
                (res: IConta[]) => {
                    this.contas = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInContas();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IConta) {
        return item.id;
    }

    registerChangeInContas() {
        this.eventSubscriber = this.eventManager.subscribe('contaListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
