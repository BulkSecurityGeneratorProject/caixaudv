import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { CantinadoreiSharedModule } from 'app/shared';
import {
    SessaoCaixaComponent,
    SessaoCaixaDetailComponent,
    SessaoCaixaUpdateComponent,
    SessaoCaixaDeletePopupComponent,
    SessaoCaixaDeleteDialogComponent,
    sessaoCaixaRoute,
    sessaoCaixaPopupRoute
} from './';

const ENTITY_STATES = [...sessaoCaixaRoute, ...sessaoCaixaPopupRoute];

@NgModule({
    imports: [CantinadoreiSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SessaoCaixaComponent,
        SessaoCaixaDetailComponent,
        SessaoCaixaUpdateComponent,
        SessaoCaixaDeleteDialogComponent,
        SessaoCaixaDeletePopupComponent
    ],
    entryComponents: [SessaoCaixaComponent, SessaoCaixaUpdateComponent, SessaoCaixaDeleteDialogComponent, SessaoCaixaDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CantinadoreiSessaoCaixaModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
