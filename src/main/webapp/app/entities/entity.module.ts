import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'produto',
                loadChildren: './produto/produto.module#CantinadoreiProdutoModule'
            },
            {
                path: 'item-compra',
                loadChildren: './item-compra/item-compra.module#CantinadoreiItemCompraModule'
            },
            {
                path: 'compra',
                loadChildren: './compra/compra.module#CantinadoreiCompraModule'
            },
            {
                path: 'sessao-caixa',
                loadChildren: './sessao-caixa/sessao-caixa.module#CantinadoreiSessaoCaixaModule'
            },
            {
                path: 'conta',
                loadChildren: './conta/conta.module#CantinadoreiContaModule'
            },
            {
                path: 'ressarcimento',
                loadChildren: './ressarcimento/ressarcimento.module#CantinadoreiRessarcimentoModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CantinadoreiEntityModule {}
