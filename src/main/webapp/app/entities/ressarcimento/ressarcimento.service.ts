import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

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
        const copy = this.convertDateFromClient(ressarcimento);
        return this.http
            .post<IRessarcimento>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(ressarcimento: IRessarcimento): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(ressarcimento);
        return this.http
            .put<IRessarcimento>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRessarcimento>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRessarcimento[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(ressarcimento: IRessarcimento): IRessarcimento {
        const copy: IRessarcimento = Object.assign({}, ressarcimento, {
            data: ressarcimento.data != null && ressarcimento.data.isValid() ? ressarcimento.data.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.data = res.body.data != null ? moment(res.body.data) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((ressarcimento: IRessarcimento) => {
                ressarcimento.data = ressarcimento.data != null ? moment(ressarcimento.data) : null;
            });
        }
        return res;
    }
}
