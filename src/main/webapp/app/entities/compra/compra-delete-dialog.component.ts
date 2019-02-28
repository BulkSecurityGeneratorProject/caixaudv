import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICompra } from 'app/shared/model/compra.model';
import { CompraService } from './compra.service';

@Component({
    selector: 'jhi-compra-delete-dialog',
    templateUrl: './compra-delete-dialog.component.html'
})
export class CompraDeleteDialogComponent {
    compra: ICompra;

    constructor(protected compraService: CompraService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.compraService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'compraListModification',
                content: 'Deleted an compra'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-compra-delete-popup',
    template: ''
})
export class CompraDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ compra }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CompraDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.compra = compra;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/compra', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/compra', { outlets: { popup: null } }]);
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
