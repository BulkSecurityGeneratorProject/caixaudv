/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CaixaudvTestModule } from '../../../test.module';
import { ContaComponent } from 'app/entities/conta/conta.component';
import { ContaService } from 'app/entities/conta/conta.service';
import { Conta } from 'app/shared/model/conta.model';

describe('Component Tests', () => {
    describe('Conta Management Component', () => {
        let comp: ContaComponent;
        let fixture: ComponentFixture<ContaComponent>;
        let service: ContaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CaixaudvTestModule],
                declarations: [ContaComponent],
                providers: []
            })
                .overrideTemplate(ContaComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ContaComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContaService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Conta(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.contas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
