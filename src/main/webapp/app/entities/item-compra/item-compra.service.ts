import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IItemCompra } from 'app/shared/model/item-compra.model';

type EntityResponseType = HttpResponse<IItemCompra>;
type EntityArrayResponseType = HttpResponse<IItemCompra[]>;

@Injectable({ providedIn: 'root' })
export class ItemCompraService {
    public resourceUrl = SERVER_API_URL + 'api/item-compras';

    constructor(protected http: HttpClient) {}

    create(itemCompra: IItemCompra): Observable<EntityResponseType> {
        return this.http.post<IItemCompra>(this.resourceUrl, itemCompra, { observe: 'response' });
    }

    update(itemCompra: IItemCompra): Observable<EntityResponseType> {
        return this.http.put<IItemCompra>(this.resourceUrl, itemCompra, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IItemCompra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IItemCompra[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
