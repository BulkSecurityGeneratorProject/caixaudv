/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CantinadoreiTestModule } from '../../../test.module';
import { RessarcimentoDetailComponent } from 'app/entities/ressarcimento/ressarcimento-detail.component';
import { Ressarcimento } from 'app/shared/model/ressarcimento.model';

describe('Component Tests', () => {
    describe('Ressarcimento Management Detail Component', () => {
        let comp: RessarcimentoDetailComponent;
        let fixture: ComponentFixture<RessarcimentoDetailComponent>;
        const route = ({ data: of({ ressarcimento: new Ressarcimento(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CantinadoreiTestModule],
                declarations: [RessarcimentoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RessarcimentoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RessarcimentoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.ressarcimento).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
