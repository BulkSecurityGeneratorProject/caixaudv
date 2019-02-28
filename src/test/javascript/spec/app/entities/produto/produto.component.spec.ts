/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CaixaudvTestModule } from '../../../test.module';
import { ProdutoComponent } from 'app/entities/produto/produto.component';
import { ProdutoService } from 'app/entities/produto/produto.service';
import { Produto } from 'app/shared/model/produto.model';

describe('Component Tests', () => {
    describe('Produto Management Component', () => {
        let comp: ProdutoComponent;
        let fixture: ComponentFixture<ProdutoComponent>;
        let service: ProdutoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CaixaudvTestModule],
                declarations: [ProdutoComponent],
                providers: []
            })
                .overrideTemplate(ProdutoComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ProdutoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProdutoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Produto(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.produtos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
