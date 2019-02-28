/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CaixaudvTestModule } from '../../../test.module';
import { ItemCompraComponent } from 'app/entities/item-compra/item-compra.component';
import { ItemCompraService } from 'app/entities/item-compra/item-compra.service';
import { ItemCompra } from 'app/shared/model/item-compra.model';

describe('Component Tests', () => {
    describe('ItemCompra Management Component', () => {
        let comp: ItemCompraComponent;
        let fixture: ComponentFixture<ItemCompraComponent>;
        let service: ItemCompraService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CaixaudvTestModule],
                declarations: [ItemCompraComponent],
                providers: []
            })
                .overrideTemplate(ItemCompraComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ItemCompraComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItemCompraService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ItemCompra(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.itemCompras[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
