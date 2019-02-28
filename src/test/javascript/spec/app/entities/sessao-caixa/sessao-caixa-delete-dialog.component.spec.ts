/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CaixaudvTestModule } from '../../../test.module';
import { SessaoCaixaDeleteDialogComponent } from 'app/entities/sessao-caixa/sessao-caixa-delete-dialog.component';
import { SessaoCaixaService } from 'app/entities/sessao-caixa/sessao-caixa.service';

describe('Component Tests', () => {
    describe('SessaoCaixa Management Delete Component', () => {
        let comp: SessaoCaixaDeleteDialogComponent;
        let fixture: ComponentFixture<SessaoCaixaDeleteDialogComponent>;
        let service: SessaoCaixaService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CaixaudvTestModule],
                declarations: [SessaoCaixaDeleteDialogComponent]
            })
                .overrideTemplate(SessaoCaixaDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SessaoCaixaDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SessaoCaixaService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
