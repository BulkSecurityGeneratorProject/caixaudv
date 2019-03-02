import { Moment } from 'moment';
import { ISessaoCaixa } from 'app/shared/model/sessao-caixa.model';
import { IConta } from 'app/shared/model/conta.model';

export interface IRessarcimento {
    id?: number;
    valor?: number;
    data?: Moment;
    sessaoCaixa?: ISessaoCaixa;
    conta?: IConta;
}

export class Ressarcimento implements IRessarcimento {
    constructor(
        public id?: number,
        public valor?: number,
        public data?: Moment,
        public sessaoCaixa?: ISessaoCaixa,
        public conta?: IConta
    ) {}
}
