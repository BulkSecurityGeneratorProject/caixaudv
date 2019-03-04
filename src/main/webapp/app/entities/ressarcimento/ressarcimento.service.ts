import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRessarcimento } from 'app/shared/model/ressarcimento.model';

type EntityResponseType = HttpResponse<IRessarcimento>;
type EntityArrayResponseType = HttpResponse<IRessarcimento[]>;

@Injectable({ providedIn: 'root' })
export class RessarcimentoService {
    public resourceUrl = SERVER_API_URL + 'api/ressarcimentos';

    constructor(protected http: HttpClient) {}

    create(ressarcimento: IRessarcimento): Observable<EntityResponseType> {
        return this.http.post<IRessarcimento>(this.resourceUrl, ressarcimento, { observe: 'response' });
    }

    update(ressarcimento: IRessarcimento): Observable<EntityResponseType> {
        return this.http.put<IRessarcimento>(this.resourceUrl, ressarcimento, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRessarcimento>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRessarcimento[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
