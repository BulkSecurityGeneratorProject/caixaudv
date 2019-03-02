import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CantinadoreiSharedModule } from 'app/shared';
import {
    RessarcimentoComponent,
    RessarcimentoDetailComponent,
    RessarcimentoUpdateComponent,
    RessarcimentoDeletePopupComponent,
    RessarcimentoDeleteDialogComponent,
    ressarcimentoRoute,
    ressarcimentoPopupRoute
} from './';

const ENTITY_STATES = [...ressarcimentoRoute, ...ressarcimentoPopupRoute];

@NgModule({
    imports: [CantinadoreiSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RessarcimentoComponent,
        RessarcimentoDetailComponent,
        RessarcimentoUpdateComponent,
        RessarcimentoDeleteDialogComponent,
        RessarcimentoDeletePopupComponent
    ],
    entryComponents: [
        RessarcimentoComponent,
        RessarcimentoUpdateComponent,
        RessarcimentoDeleteDialogComponent,
        RessarcimentoDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CantinadoreiRessarcimentoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
