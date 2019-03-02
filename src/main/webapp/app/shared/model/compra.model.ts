import { ISessaoCaixa } from 'app/shared/model/sessao-caixa.model';
import { IConta } from 'app/shared/model/conta.model';
import { IItemCompra } from 'app/shared/model/item-compra.model';

export interface ICompra {
    id?: number;
    valorTotal?: number;
    nomeSolic?: string;
    sessaoCaixa?: ISessaoCaixa;
    conta?: IConta;
    itensCompras?: IItemCompra[];
}

export class Compra implements ICompra {
    constructor(
        public id?: number,
        public valorTotal?: number,
        public nomeSolic?: string,
        public sessaoCaixa?: ISessaoCaixa,
        public conta?: IConta,
        public itensCompras?: IItemCompra[]
    ) {}
}
