/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CantinadoreiTestModule } from '../../../test.module';
import { SessaoCaixaUpdateComponent } from 'app/entities/sessao-caixa/sessao-caixa-update.component';
import { SessaoCaixaService } from 'app/entities/sessao-caixa/sessao-caixa.service';
import { SessaoCaixa } from 'app/shared/model/sessao-caixa.model';

describe('Component Tests', () => {
    describe('SessaoCaixa Management Update Component', () => {
        let comp: SessaoCaixaUpdateComponent;
        let fixture: ComponentFixture<SessaoCaixaUpdateComponent>;
        let service: SessaoCaixaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CantinadoreiTestModule],
                declarations: [SessaoCaixaUpdateComponent]
            })
                .overrideTemplate(SessaoCaixaUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SessaoCaixaUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SessaoCaixaService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SessaoCaixa(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.sessaoCaixa = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SessaoCaixa();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.sessaoCaixa = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
