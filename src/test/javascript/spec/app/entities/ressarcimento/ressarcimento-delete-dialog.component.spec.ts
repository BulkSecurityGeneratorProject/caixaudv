/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CaixaudvTestModule } from '../../../test.module';
import { RessarcimentoDeleteDialogComponent } from 'app/entities/ressarcimento/ressarcimento-delete-dialog.component';
import { RessarcimentoService } from 'app/entities/ressarcimento/ressarcimento.service';

describe('Component Tests', () => {
    describe('Ressarcimento Management Delete Component', () => {
        let comp: RessarcimentoDeleteDialogComponent;
        let fixture: ComponentFixture<RessarcimentoDeleteDialogComponent>;
        let service: RessarcimentoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CaixaudvTestModule],
                declarations: [RessarcimentoDeleteDialogComponent]
            })
                .overrideTemplate(RessarcimentoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RessarcimentoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RessarcimentoService);
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
