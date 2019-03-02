import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISessaoCaixa } from 'app/shared/model/sessao-caixa.model';
import { SessaoCaixaService } from './sessao-caixa.service';

@Component({
    selector: 'jhi-sessao-caixa-delete-dialog',
    templateUrl: './sessao-caixa-delete-dialog.component.html'
})
export class SessaoCaixaDeleteDialogComponent {
    sessaoCaixa: ISessaoCaixa;

    constructor(
        protected sessaoCaixaService: SessaoCaixaService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.sessaoCaixaService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'sessaoCaixaListModification',
                content: 'Deleted an sessaoCaixa'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sessao-caixa-delete-popup',
    template: ''
})
export class SessaoCaixaDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ sessaoCaixa }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SessaoCaixaDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.sessaoCaixa = sessaoCaixa;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sessao-caixa', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sessao-caixa', { outlets: { popup: null } }]);
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
