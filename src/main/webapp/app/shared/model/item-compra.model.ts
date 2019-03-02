import { IProduto } from 'app/shared/model/produto.model';
import { ICompra } from 'app/shared/model/compra.model';

export interface IItemCompra {
    id?: number;
    quant?: number;
    produto?: IProduto;
    compra?: ICompra;
}

export class ItemCompra implements IItemCompra {
    constructor(public id?: number, public quant?: number, public produto?: IProduto, public compra?: ICompra) {}
}
