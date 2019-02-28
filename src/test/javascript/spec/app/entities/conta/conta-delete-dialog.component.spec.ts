/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CaixaudvTestModule } from '../../../test.module';
import { ContaDeleteDialogComponent } from 'app/entities/conta/conta-delete-dialog.component';
import { ContaService } from 'app/entities/conta/conta.service';

describe('Component Tests', () => {
    describe('Conta Management Delete Component', () => {
        let comp: ContaDeleteDialogComponent;
        let fixture: ComponentFixture<ContaDeleteDialogComponent>;
        let service: ContaService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CaixaudvTestModule],
                declarations: [ContaDeleteDialogComponent]
            })
                .overrideTemplate(ContaDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ContaDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContaService);
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