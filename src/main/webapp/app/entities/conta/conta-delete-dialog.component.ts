import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConta } from 'app/shared/model/conta.model';
import { ContaService } from './conta.service';

@Component({
    selector: 'jhi-conta-delete-dialog',
    templateUrl: './conta-delete-dialog.component.html'
})
export class ContaDeleteDialogComponent {
    conta: IConta;

    constructor(protected contaService: ContaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.contaService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'contaListModification',
                content: 'Deleted an conta'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-conta-delete-popup',
    template: ''
})
export class ContaDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ conta }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ContaDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.conta = conta;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/conta', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/conta', { outlets: { popup: null } }]);
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
