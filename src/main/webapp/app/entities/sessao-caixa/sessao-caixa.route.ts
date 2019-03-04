import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SessaoCaixa } from 'app/shared/model/sessao-caixa.model';
import { SessaoCaixaService } from './sessao-caixa.service';
import { SessaoCaixaComponent } from './sessao-caixa.component';
import { SessaoCaixaDetailComponent } from './sessao-caixa-detail.component';
import { SessaoCaixaUpdateComponent } from './sessao-caixa-update.component';
import { SessaoCaixaDeletePopupComponent } from './sessao-caixa-delete-dialog.component';
import { ISessaoCaixa } from 'app/shared/model/sessao-caixa.model';

@Injectable({ providedIn: 'root' })
export class SessaoCaixaResolve implements Resolve<ISessaoCaixa> {
    constructor(private service: SessaoCaixaService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISessaoCaixa> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SessaoCaixa>) => response.ok),
                map((sessaoCaixa: HttpResponse<SessaoCaixa>) => sessaoCaixa.body)
            );
        }
        return of(new SessaoCaixa());
    }
}

export const sessaoCaixaRoute: Routes = [
    {
        path: '',
        component: SessaoCaixaComponent,
        data: {
            authorities: ['ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CANTINIER', 'ROLE_CLIENT'],
            pageTitle: 'cantinadoreiApp.sessaoCaixa.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SessaoCaixaDetailComponent,
        resolve: {
            sessaoCaixa: SessaoCaixaResolve
        },
        data: {
            authorities: ['ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CANTINIER', 'ROLE_CLIENT'],
            pageTitle: 'cantinadoreiApp.sessaoCaixa.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SessaoCaixaUpdateComponent,
        resolve: {
            sessaoCaixa: SessaoCaixaResolve
        },
        data: {
            authorities: ['ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CANTINIER', 'ROLE_CLIENT'],
            pageTitle: 'cantinadoreiApp.sessaoCaixa.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SessaoCaixaUpdateComponent,
        resolve: {
            sessaoCaixa: SessaoCaixaResolve
        },
        data: {
            authorities: ['ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CANTINIER', 'ROLE_CLIENT'],
            pageTitle: 'cantinadoreiApp.sessaoCaixa.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sessaoCaixaPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SessaoCaixaDeletePopupComponent,
        resolve: {
            sessaoCaixa: SessaoCaixaResolve
        },
        data: {
            authorities: ['ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CANTINIER', 'ROLE_CLIENT'],
            pageTitle: 'cantinadoreiApp.sessaoCaixa.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
