import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ItemCompra } from 'app/shared/model/item-compra.model';
import { ItemCompraService } from './item-compra.service';
import { ItemCompraComponent } from './item-compra.component';
import { ItemCompraDetailComponent } from './item-compra-detail.component';
import { ItemCompraUpdateComponent } from './item-compra-update.component';
import { ItemCompraDeletePopupComponent } from './item-compra-delete-dialog.component';
import { IItemCompra } from 'app/shared/model/item-compra.model';

@Injectable({ providedIn: 'root' })
export class ItemCompraResolve implements Resolve<IItemCompra> {
    constructor(private service: ItemCompraService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IItemCompra> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ItemCompra>) => response.ok),
                map((itemCompra: HttpResponse<ItemCompra>) => itemCompra.body)
            );
        }
        return of(new ItemCompra());
    }
}

export const itemCompraRoute: Routes = [
    {
        path: '',
        component: ItemCompraComponent,
        data: {
            authorities: ['ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CANTINIER', 'ROLE_CLIENT'],
            pageTitle: 'cantinadoreiApp.itemCompra.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ItemCompraDetailComponent,
        resolve: {
            itemCompra: ItemCompraResolve
        },
        data: {
            authorities: ['ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CANTINIER', 'ROLE_CLIENT'],
            pageTitle: 'cantinadoreiApp.itemCompra.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ItemCompraUpdateComponent,
        resolve: {
            itemCompra: ItemCompraResolve
        },
        data: {
            authorities: ['ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CANTINIER', 'ROLE_CLIENT'],
            pageTitle: 'cantinadoreiApp.itemCompra.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ItemCompraUpdateComponent,
        resolve: {
            itemCompra: ItemCompraResolve
        },
        data: {
            authorities: ['ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CANTINIER', 'ROLE_CLIENT'],
            pageTitle: 'cantinadoreiApp.itemCompra.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const itemCompraPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ItemCompraDeletePopupComponent,
        resolve: {
            itemCompra: ItemCompraResolve
        },
        data: {
            authorities: ['ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CANTINIER', 'ROLE_CLIENT'],
            pageTitle: 'cantinadoreiApp.itemCompra.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
