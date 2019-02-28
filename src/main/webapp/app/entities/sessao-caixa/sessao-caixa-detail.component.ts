import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISessaoCaixa } from 'app/shared/model/sessao-caixa.model';

@Component({
    selector: 'jhi-sessao-caixa-detail',
    templateUrl: './sessao-caixa-detail.component.html'
})
export class SessaoCaixaDetailComponent implements OnInit {
    sessaoCaixa: ISessaoCaixa;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sessaoCaixa }) => {
            this.sessaoCaixa = sessaoCaixa;
        });
    }

    previousState() {
        window.history.back();
    }
}
