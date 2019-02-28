/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CaixaudvTestModule } from '../../../test.module';
import { RessarcimentoComponent } from 'app/entities/ressarcimento/ressarcimento.component';
import { RessarcimentoService } from 'app/entities/ressarcimento/ressarcimento.service';
import { Ressarcimento } from 'app/shared/model/ressarcimento.model';

describe('Component Tests', () => {
    describe('Ressarcimento Management Component', () => {
        let comp: RessarcimentoComponent;
        let fixture: ComponentFixture<RessarcimentoComponent>;
        let service: RessarcimentoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CaixaudvTestModule],
                declarations: [RessarcimentoComponent],
                providers: []
            })
                .overrideTemplate(RessarcimentoComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RessarcimentoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RessarcimentoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Ressarcimento(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.ressarcimentos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
