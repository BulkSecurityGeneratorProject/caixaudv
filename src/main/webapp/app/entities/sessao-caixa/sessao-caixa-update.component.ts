import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { ISessaoCaixa } from 'app/shared/model/sessao-caixa.model';
import { SessaoCaixaService } from './sessao-caixa.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-sessao-caixa-update',
    templateUrl: './sessao-caixa-update.component.html'
})
export class SessaoCaixaUpdateComponent implements OnInit {
    sessaoCaixa: ISessaoCaixa;
    isSaving: boolean;

    users: IUser[];
    dataDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected sessaoCaixaService: SessaoCaixaService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sessaoCaixa }) => {
            this.sessaoCaixa = sessaoCaixa;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.sessaoCaixa.id !== undefined) {
            this.subscribeToSaveResponse(this.sessaoCaixaService.update(this.sessaoCaixa));
        } else {
            this.subscribeToSaveResponse(this.sessaoCaixaService.create(this.sessaoCaixa));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISessaoCaixa>>) {
        result.subscribe((res: HttpResponse<ISessaoCaixa>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
