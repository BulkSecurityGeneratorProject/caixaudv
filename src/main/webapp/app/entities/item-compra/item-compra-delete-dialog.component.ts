import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IItemCompra } from 'app/shared/model/item-compra.model';
import { ItemCompraService } from './item-compra.service';

@Component({
    selector: 'jhi-item-compra-delete-dialog',
    templateUrl: './item-compra-delete-dialog.component.html'
})
export class ItemCompraDeleteDialogComponent {
    itemCompra: IItemCompra;

    constructor(
        protected itemCompraService: ItemCompraService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.itemCompraService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'itemCompraListModification',
                content: 'Deleted an itemCompra'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-item-compra-delete-popup',
    template: ''
})
export class ItemCompraDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ itemCompra }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ItemCompraDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.itemCompra = itemCompra;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/item-compra', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/item-compra', { outlets: { popup: null } }]);
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
