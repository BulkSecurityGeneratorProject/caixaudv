import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IRessarcimento } from 'app/shared/model/ressarcimento.model';
import { ICompra } from 'app/shared/model/compra.model';

export interface IConta {
    id?: number;
    saldoAtual?: number;
    dataAbertura?: Moment;
    user?: IUser;
    ressarcimentos?: IRessarcimento[];
    compras?: ICompra[];
}

export class Conta implements IConta {
    constructor(
        public id?: number,
        public saldoAtual?: number,
        public dataAbertura?: Moment,
        public user?: IUser,
        public ressarcimentos?: IRessarcimento[],
        public compras?: ICompra[]
    ) {}
}
