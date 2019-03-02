/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CantinadoreiTestModule } from '../../../test.module';
import { SessaoCaixaDetailComponent } from 'app/entities/sessao-caixa/sessao-caixa-detail.component';
import { SessaoCaixa } from 'app/shared/model/sessao-caixa.model';

describe('Component Tests', () => {
    describe('SessaoCaixa Management Detail Component', () => {
        let comp: SessaoCaixaDetailComponent;
        let fixture: ComponentFixture<SessaoCaixaDetailComponent>;
        const route = ({ data: of({ sessaoCaixa: new SessaoCaixa(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CantinadoreiTestModule],
                declarations: [SessaoCaixaDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SessaoCaixaDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SessaoCaixaDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.sessaoCaixa).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
