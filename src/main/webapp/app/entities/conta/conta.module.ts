import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CantinadoreiSharedModule } from 'app/shared';
import {
    ContaComponent,
    ContaDetailComponent,
    ContaUpdateComponent,
    ContaDeletePopupComponent,
    ContaDeleteDialogComponent,
    contaRoute,
    contaPopupRoute
} from './';

const ENTITY_STATES = [...contaRoute, ...contaPopupRoute];

@NgModule({
    imports: [CantinadoreiSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ContaComponent, ContaDetailComponent, ContaUpdateComponent, ContaDeleteDialogComponent, ContaDeletePopupComponent],
    entryComponents: [ContaComponent, ContaUpdateComponent, ContaDeleteDialogComponent, ContaDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CantinadoreiContaModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
