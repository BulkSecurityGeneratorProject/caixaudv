import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRessarcimento } from 'app/shared/model/ressarcimento.model';
import { RessarcimentoService } from './ressarcimento.service';

@Component({
    selector: 'jhi-ressarcimento-delete-dialog',
    templateUrl: './ressarcimento-delete-dialog.component.html'
})
export class RessarcimentoDeleteDialogComponent {
    ressarcimento: IRessarcimento;

    constructor(
        protected ressarcimentoService: RessarcimentoService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ressarcimentoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'ressarcimentoListModification',
                content: 'Deleted an ressarcimento'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ressarcimento-delete-popup',
    template: ''
})
export class RessarcimentoDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ ressarcimento }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RessarcimentoDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.ressarcimento = ressarcimento;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/ressarcimento', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/ressarcimento', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
