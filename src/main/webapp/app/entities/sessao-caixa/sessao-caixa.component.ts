import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISessaoCaixa } from 'app/shared/model/sessao-caixa.model';
import { AccountService } from 'app/core';
import { SessaoCaixaService } from './sessao-caixa.service';

@Component({
    selector: 'jhi-sessao-caixa',
    templateUrl: './sessao-caixa.component.html'
})
export class SessaoCaixaComponent implements OnInit, OnDestroy {
    sessaoCaixas: ISessaoCaixa[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected sessaoCaixaService: SessaoCaixaService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.sessaoCaixaService
            .query()
            .pipe(
                filter((res: HttpResponse<ISessaoCaixa[]>) => res.ok),
                map((res: HttpResponse<ISessaoCaixa[]>) => res.body)
            )
            .subscribe(
                (res: ISessaoCaixa[]) => {
                    this.sessaoCaixas = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSessaoCaixas();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISessaoCaixa) {
        return item.id;
    }

    registerChangeInSessaoCaixas() {
        this.eventSubscriber = this.eventManager.subscribe('sessaoCaixaListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
