import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IRessarcimento } from 'app/shared/model/ressarcimento.model';
import { RessarcimentoService } from './ressarcimento.service';
import { ISessaoCaixa } from 'app/shared/model/sessao-caixa.model';
import { SessaoCaixaService } from 'app/entities/sessao-caixa';
import { IConta } from 'app/shared/model/conta.model';
import { ContaService } from 'app/entities/conta';

@Component({
    selector: 'jhi-ressarcimento-update',
    templateUrl: './ressarcimento-update.component.html'
})
export class RessarcimentoUpdateComponent implements OnInit {
    ressarcimento: IRessarcimento;
    isSaving: boolean;

    sessaocaixas: ISessaoCaixa[];

    contas: IConta[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected ressarcimentoService: RessarcimentoService,
        protected sessaoCaixaService: SessaoCaixaService,
        protected contaService: ContaService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ ressarcimento }) => {
            this.ressarcimento = ressarcimento;
        });
        this.sessaoCaixaService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ISessaoCaixa[]>) => mayBeOk.ok),
                map((response: HttpResponse<ISessaoCaixa[]>) => response.body)
            )
            .subscribe((res: ISessaoCaixa[]) => (this.sessaocaixas = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.contaService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IConta[]>) => mayBeOk.ok),
                map((response: HttpResponse<IConta[]>) => response.body)
            )
            .subscribe((res: IConta[]) => (this.contas = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.ressarcimento.id !== undefined) {
            this.subscribeToSaveResponse(this.ressarcimentoService.update(this.ressarcimento));
        } else {
            this.subscribeToSaveResponse(this.ressarcimentoService.create(this.ressarcimento));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRessarcimento>>) {
        result.subscribe((res: HttpResponse<IRessarcimento>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackSessaoCaixaById(index: number, item: ISessaoCaixa) {
        return item.id;
    }

    trackContaById(index: number, item: IConta) {
        return item.id;
    }
}
