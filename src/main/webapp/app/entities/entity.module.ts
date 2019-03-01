import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'produto',
                loadChildren: './produto/produto.module#CaixaudvProdutoModule'
            },
            {
                path: 'item-compra',
                loadChildren: './item-compra/item-compra.module#CaixaudvItemCompraModule'
            },
            {
                path: 'compra',
                loadChildren: './compra/compra.module#CaixaudvCompraModule'
            },
            {
                path: 'sessao-caixa',
                loadChildren: './sessao-caixa/sessao-caixa.module#CaixaudvSessaoCaixaModule'
            },
            {
                path: 'conta',
                loadChildren: './conta/conta.module#CaixaudvContaModule'
            },
            {
                path: 'ressarcimento',
                loadChildren: './ressarcimento/ressarcimento.module#CaixaudvRessarcimentoModule'
            },
            {
                path: 'conta',
                loadChildren: './conta/conta.module#CaixaudvContaModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CaixaudvEntityModule {}
