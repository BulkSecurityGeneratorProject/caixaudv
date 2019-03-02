/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CantinadoreiTestModule } from '../../../test.module';
import { RessarcimentoUpdateComponent } from 'app/entities/ressarcimento/ressarcimento-update.component';
import { RessarcimentoService } from 'app/entities/ressarcimento/ressarcimento.service';
import { Ressarcimento } from 'app/shared/model/ressarcimento.model';

describe('Component Tests', () => {
    describe('Ressarcimento Management Update Component', () => {
        let comp: RessarcimentoUpdateComponent;
        let fixture: ComponentFixture<RessarcimentoUpdateComponent>;
        let service: RessarcimentoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CantinadoreiTestModule],
                declarations: [RessarcimentoUpdateComponent]
            })
                .overrideTemplate(RessarcimentoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RessarcimentoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RessarcimentoService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Ressarcimento(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.ressarcimento = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Ressarcimento();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.ressarcimento = entity;
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
