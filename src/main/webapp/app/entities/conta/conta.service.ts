import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IConta } from 'app/shared/model/conta.model';

type EntityResponseType = HttpResponse<IConta>;
type EntityArrayResponseType = HttpResponse<IConta[]>;

@Injectable({ providedIn: 'root' })
export class ContaService {
    public resourceUrl = SERVER_API_URL + 'api/contas';

    constructor(protected http: HttpClient) {}

    create(conta: IConta): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(conta);
        return this.http
            .post<IConta>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(conta: IConta): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(conta);
        return this.http
            .put<IConta>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IConta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IConta[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(conta: IConta): IConta {
        const copy: IConta = Object.assign({}, conta, {
            dataAbertura: conta.dataAbertura != null && conta.dataAbertura.isValid() ? conta.dataAbertura.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dataAbertura = res.body.dataAbertura != null ? moment(res.body.dataAbertura) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((conta: IConta) => {
                conta.dataAbertura = conta.dataAbertura != null ? moment(conta.dataAbertura) : null;
            });
        }
        return res;
    }
}
