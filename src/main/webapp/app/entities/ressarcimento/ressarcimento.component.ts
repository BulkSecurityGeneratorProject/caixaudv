import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IRessarcimento } from 'app/shared/model/ressarcimento.model';
import { AccountService } from 'app/core';
import { RessarcimentoService } from './ressarcimento.service';

@Component({
    selector: 'jhi-ressarcimento',
    templateUrl: './ressarcimento.component.html'
})
export class RessarcimentoComponent implements OnInit, OnDestroy {
    ressarcimentos: IRessarcimento[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected ressarcimentoService: RessarcimentoService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.ressarcimentoService
            .query()
            .pipe(
                filter((res: HttpResponse<IRessarcimento[]>) => res.ok),
                map((res: HttpResponse<IRessarcimento[]>) => res.body)
            )
            .subscribe(
                (res: IRessarcimento[]) => {
                    this.ressarcimentos = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInRessarcimentos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IRessarcimento) {
        return item.id;
    }

    registerChangeInRessarcimentos() {
        this.eventSubscriber = this.eventManager.subscribe('ressarcimentoListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
