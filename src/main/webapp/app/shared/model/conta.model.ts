import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IRessarcimento } from 'app/shared/model/ressarcimento.model';
import { ICompra } from 'app/shared/model/compra.model';

export const enum NivelPermissao {
    ADMIN = 'ADMIN',
    OPERADOR = 'OPERADOR',
    CANTINEIRO = 'CANTINEIRO',
    CLIENTE = 'CLIENTE'
}

export interface IConta {
    id?: number;
    saldoAtual?: number;
    dataAbertura?: Moment;
    nivelPermissao?: NivelPermissao;
    user?: IUser;
    ressarcimentos?: IRessarcimento[];
    compras?: ICompra[];
}

export class Conta implements IConta {
    constructor(
        public id?: number,
        public saldoAtual?: number,
        public dataAbertura?: Moment,
        public nivelPermissao?: NivelPermissao,
        public user?: IUser,
        public ressarcimentos?: IRessarcimento[],
        public compras?: ICompra[]
    ) {}
}
