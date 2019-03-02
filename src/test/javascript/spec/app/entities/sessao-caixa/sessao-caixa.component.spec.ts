/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CantinadoreiTestModule } from '../../../test.module';
import { SessaoCaixaComponent } from 'app/entities/sessao-caixa/sessao-caixa.component';
import { SessaoCaixaService } from 'app/entities/sessao-caixa/sessao-caixa.service';
import { SessaoCaixa } from 'app/shared/model/sessao-caixa.model';

describe('Component Tests', () => {
    describe('SessaoCaixa Management Component', () => {
        let comp: SessaoCaixaComponent;
        let fixture: ComponentFixture<SessaoCaixaComponent>;
        let service: SessaoCaixaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CantinadoreiTestModule],
                declarations: [SessaoCaixaComponent],
                providers: []
            })
                .overrideTemplate(SessaoCaixaComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SessaoCaixaComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SessaoCaixaService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SessaoCaixa(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.sessaoCaixas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
