import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { ICompra } from 'app/shared/model/compra.model';
import { IRessarcimento } from 'app/shared/model/ressarcimento.model';

export interface ISessaoCaixa {
    id?: number;
    valorAtual?: number;
    data?: Moment;
    user?: IUser;
    compras?: ICompra[];
    ressarcimentos?: IRessarcimento[];
}

export class SessaoCaixa implements ISessaoCaixa {
    constructor(
        public id?: number,
        public valorAtual?: number,
        public data?: Moment,
        public user?: IUser,
        public compras?: ICompra[],
        public ressarcimentos?: IRessarcimento[]
    ) {}
}
