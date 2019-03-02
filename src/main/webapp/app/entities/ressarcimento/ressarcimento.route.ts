import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Ressarcimento } from 'app/shared/model/ressarcimento.model';
import { RessarcimentoService } from './ressarcimento.service';
import { RessarcimentoComponent } from './ressarcimento.component';
import { RessarcimentoDetailComponent } from './ressarcimento-detail.component';
import { RessarcimentoUpdateComponent } from './ressarcimento-update.component';
import { RessarcimentoDeletePopupComponent } from './ressarcimento-delete-dialog.component';
import { IRessarcimento } from 'app/shared/model/ressarcimento.model';

@Injectable({ providedIn: 'root' })
export class RessarcimentoResolve implements Resolve<IRessarcimento> {
    constructor(private service: RessarcimentoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRessarcimento> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Ressarcimento>) => response.ok),
                map((ressarcimento: HttpResponse<Ressarcimento>) => ressarcimento.body)
            );
        }
        return of(new Ressarcimento());
    }
}

export const ressarcimentoRoute: Routes = [
    {
        path: '',
        component: RessarcimentoComponent,
        data: {
            authorities: ['ROLE_OPERATOR'],
            pageTitle: 'cantinadoreiApp.ressarcimento.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: RessarcimentoDetailComponent,
        resolve: {
            ressarcimento: RessarcimentoResolve
        },
        data: {
            authorities: ['ROLE_OPERATOR'],
            pageTitle: 'cantinadoreiApp.ressarcimento.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: RessarcimentoUpdateComponent,
        resolve: {
            ressarcimento: RessarcimentoResolve
        },
        data: {
            authorities: ['ROLE_OPERATOR'],
            pageTitle: 'cantinadoreiApp.ressarcimento.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: RessarcimentoUpdateComponent,
        resolve: {
            ressarcimento: RessarcimentoResolve
        },
        data: {
            authorities: ['ROLE_OPERATOR'],
            pageTitle: 'cantinadoreiApp.ressarcimento.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ressarcimentoPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: RessarcimentoDeletePopupComponent,
        resolve: {
            ressarcimento: RessarcimentoResolve
        },
        data: {
            authorities: ['ROLE_OPERATOR'],
            pageTitle: 'cantinadoreiApp.ressarcimento.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
