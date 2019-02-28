import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICompra } from 'app/shared/model/compra.model';
import { CompraService } from './compra.service';
import { ISessaoCaixa } from 'app/shared/model/sessao-caixa.model';
import { SessaoCaixaService } from 'app/entities/sessao-caixa';
import { IConta } from 'app/shared/model/conta.model';
import { ContaService } from 'app/entities/conta';

@Component({
    selector: 'jhi-compra-update',
    templateUrl: './compra-update.component.html'
})
export class CompraUpdateComponent implements OnInit {
    compra: ICompra;
    isSaving: boolean;

    sessaocaixas: ISessaoCaixa[];

    contas: IConta[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected compraService: CompraService,
        protected sessaoCaixaService: SessaoCaixaService,
        protected contaService: ContaService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ compra }) => {
            this.compra = compra;
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
        if (this.compra.id !== undefined) {
            this.subscribeToSaveResponse(this.compraService.update(this.compra));
        } else {
            this.subscribeToSaveResponse(this.compraService.create(this.compra));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompra>>) {
        result.subscribe((res: HttpResponse<ICompra>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
