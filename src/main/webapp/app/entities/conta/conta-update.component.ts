import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IConta } from 'app/shared/model/conta.model';
import { ContaService } from './conta.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-conta-update',
    templateUrl: './conta-update.component.html'
})
export class ContaUpdateComponent implements OnInit {
    conta: IConta;
    isSaving: boolean;

    users: IUser[];
    dataAberturaDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected contaService: ContaService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ conta }) => {
            this.conta = conta;
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
        if (this.conta.id !== undefined) {
            this.subscribeToSaveResponse(this.contaService.update(this.conta));
        } else {
            this.subscribeToSaveResponse(this.contaService.create(this.conta));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IConta>>) {
        result.subscribe((res: HttpResponse<IConta>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
