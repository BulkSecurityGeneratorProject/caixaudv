import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CaixaudvSharedModule } from 'app/shared';
import {
    ItemCompraComponent,
    ItemCompraDetailComponent,
    ItemCompraUpdateComponent,
    ItemCompraDeletePopupComponent,
    ItemCompraDeleteDialogComponent,
    itemCompraRoute,
    itemCompraPopupRoute
} from './';

const ENTITY_STATES = [...itemCompraRoute, ...itemCompraPopupRoute];

@NgModule({
    imports: [CaixaudvSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ItemCompraComponent,
        ItemCompraDetailComponent,
        ItemCompraUpdateComponent,
        ItemCompraDeleteDialogComponent,
        ItemCompraDeletePopupComponent
    ],
    entryComponents: [ItemCompraComponent, ItemCompraUpdateComponent, ItemCompraDeleteDialogComponent, ItemCompraDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CaixaudvItemCompraModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
