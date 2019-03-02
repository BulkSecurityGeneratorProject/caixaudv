import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IItemCompra } from 'app/shared/model/item-compra.model';
import { ItemCompraService } from './item-compra.service';
import { IProduto } from 'app/shared/model/produto.model';
import { ProdutoService } from 'app/entities/produto';
import { ICompra } from 'app/shared/model/compra.model';
import { CompraService } from 'app/entities/compra';

@Component({
    selector: 'jhi-item-compra-update',
    templateUrl: './item-compra-update.component.html'
})
export class ItemCompraUpdateComponent implements OnInit {
    itemCompra: IItemCompra;
    isSaving: boolean;

    produtos: IProduto[];

    compras: ICompra[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected itemCompraService: ItemCompraService,
        protected produtoService: ProdutoService,
        protected compraService: CompraService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ itemCompra }) => {
            this.itemCompra = itemCompra;
        });
        this.produtoService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IProduto[]>) => mayBeOk.ok),
                map((response: HttpResponse<IProduto[]>) => response.body)
            )
            .subscribe((res: IProduto[]) => (this.produtos = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.compraService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICompra[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICompra[]>) => response.body)
            )
            .subscribe((res: ICompra[]) => (this.compras = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.itemCompra.id !== undefined) {
            this.subscribeToSaveResponse(this.itemCompraService.update(this.itemCompra));
        } else {
            this.subscribeToSaveResponse(this.itemCompraService.create(this.itemCompra));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemCompra>>) {
        result.subscribe((res: HttpResponse<IItemCompra>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackProdutoById(index: number, item: IProduto) {
        return item.id;
    }

    trackCompraById(index: number, item: ICompra) {
        return item.id;
    }
}
