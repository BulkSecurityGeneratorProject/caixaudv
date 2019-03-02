import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Conta } from 'app/shared/model/conta.model';
import { ContaService } from './conta.service';
import { ContaComponent } from './conta.component';
import { ContaDetailComponent } from './conta-detail.component';
import { ContaUpdateComponent } from './conta-update.component';
import { ContaDeletePopupComponent } from './conta-delete-dialog.component';
import { IConta } from 'app/shared/model/conta.model';

@Injectable({ providedIn: 'root' })
export class ContaResolve implements Resolve<IConta> {
    constructor(private service: ContaService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IConta> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Conta>) => response.ok),
                map((conta: HttpResponse<Conta>) => conta.body)
            );
        }
        return of(new Conta());
    }
}

export const contaRoute: Routes = [
    {
        path: '',
        component: ContaComponent,
        data: {
            authorities: ['ROLE_OPERATOR'],
            pageTitle: 'cantinadoreiApp.conta.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ContaDetailComponent,
        resolve: {
            conta: ContaResolve
        },
        data: {
            authorities: ['ROLE_OPERATOR'],
            pageTitle: 'cantinadoreiApp.conta.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ContaUpdateComponent,
        resolve: {
            conta: ContaResolve
        },
        data: {
            authorities: ['ROLE_OPERATOR'],
            pageTitle: 'cantinadoreiApp.conta.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ContaUpdateComponent,
        resolve: {
            conta: ContaResolve
        },
        data: {
            authorities: ['ROLE_OPERATOR'],
            pageTitle: 'cantinadoreiApp.conta.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contaPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ContaDeletePopupComponent,
        resolve: {
            conta: ContaResolve
        },
        data: {
            authorities: ['ROLE_OPERATOR'],
            pageTitle: 'cantinadoreiApp.conta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
