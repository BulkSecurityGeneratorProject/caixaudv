import { IItemCompra } from 'app/shared/model/item-compra.model';

export interface IProduto {
    id?: number;
    nome?: string;
    preco?: number;
    descricao?: string;
    itensCompras?: IItemCompra[];
}

export class Produto implements IProduto {
    constructor(
        public id?: number,
        public nome?: string,
        public preco?: number,
        public descricao?: string,
        public itensCompras?: IItemCompra[]
    ) {}
}
