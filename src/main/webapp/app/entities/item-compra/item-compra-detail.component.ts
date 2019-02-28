import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemCompra } from 'app/shared/model/item-compra.model';

@Component({
    selector: 'jhi-item-compra-detail',
    templateUrl: './item-compra-detail.component.html'
})
export class ItemCompraDetailComponent implements OnInit {
    itemCompra: IItemCompra;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ itemCompra }) => {
            this.itemCompra = itemCompra;
        });
    }

    previousState() {
        window.history.back();
    }
}
