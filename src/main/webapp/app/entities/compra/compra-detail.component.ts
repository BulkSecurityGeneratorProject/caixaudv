import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompra } from 'app/shared/model/compra.model';

@Component({
    selector: 'jhi-compra-detail',
    templateUrl: './compra-detail.component.html'
})
export class CompraDetailComponent implements OnInit {
    compra: ICompra;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ compra }) => {
            this.compra = compra;
        });
    }

    previousState() {
        window.history.back();
    }
}
