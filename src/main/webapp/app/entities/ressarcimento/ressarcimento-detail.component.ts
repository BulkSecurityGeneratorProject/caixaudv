import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRessarcimento } from 'app/shared/model/ressarcimento.model';

@Component({
    selector: 'jhi-ressarcimento-detail',
    templateUrl: './ressarcimento-detail.component.html'
})
export class RessarcimentoDetailComponent implements OnInit {
    ressarcimento: IRessarcimento;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ ressarcimento }) => {
            this.ressarcimento = ressarcimento;
        });
    }

    previousState() {
        window.history.back();
    }
}
