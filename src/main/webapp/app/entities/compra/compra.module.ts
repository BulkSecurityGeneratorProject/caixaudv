import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CaixaudvSharedModule } from 'app/shared';
import {
    CompraComponent,
    CompraDetailComponent,
    CompraUpdateComponent,
    CompraDeletePopupComponent,
    CompraDeleteDialogComponent,
    compraRoute,
    compraPopupRoute
} from './';

const ENTITY_STATES = [...compraRoute, ...compraPopupRoute];

@NgModule({
    imports: [CaixaudvSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [CompraComponent, CompraDetailComponent, CompraUpdateComponent, CompraDeleteDialogComponent, CompraDeletePopupComponent],
    entryComponents: [CompraComponent, CompraUpdateComponent, CompraDeleteDialogComponent, CompraDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CaixaudvCompraModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
