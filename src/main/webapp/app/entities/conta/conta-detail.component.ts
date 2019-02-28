import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConta } from 'app/shared/model/conta.model';

@Component({
    selector: 'jhi-conta-detail',
    templateUrl: './conta-detail.component.html'
})
export class ContaDetailComponent implements OnInit {
    conta: IConta;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ conta }) => {
            this.conta = conta;
        });
    }

    previousState() {
        window.history.back();
    }
}
