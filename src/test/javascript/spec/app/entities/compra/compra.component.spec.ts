/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CaixaudvTestModule } from '../../../test.module';
import { CompraComponent } from 'app/entities/compra/compra.component';
import { CompraService } from 'app/entities/compra/compra.service';
import { Compra } from 'app/shared/model/compra.model';

describe('Component Tests', () => {
    describe('Compra Management Component', () => {
        let comp: CompraComponent;
        let fixture: ComponentFixture<CompraComponent>;
        let service: CompraService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CaixaudvTestModule],
                declarations: [CompraComponent],
                providers: []
            })
                .overrideTemplate(CompraComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CompraComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompraService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Compra(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.compras[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
