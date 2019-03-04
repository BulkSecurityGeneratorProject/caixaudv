import { ISessaoCaixa } from 'app/shared/model/sessao-caixa.model';
import { IConta } from 'app/shared/model/conta.model';

export interface IRessarcimento {
    id?: number;
    valor?: number;
    sessaoCaixa?: ISessaoCaixa;
    conta?: IConta;
}

export class Ressarcimento implements IRessarcimento {
    constructor(public id?: number, public valor?: number, public sessaoCaixa?: ISessaoCaixa, public conta?: IConta) {}
}
