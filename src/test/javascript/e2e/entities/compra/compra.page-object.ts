import { element, by, ElementFinder } from 'protractor';

export class CompraComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-compra div table .btn-danger'));
    title = element.all(by.css('jhi-compra div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CompraUpdatePage {
    pageTitle = element(by.id('jhi-compra-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    valorTotalInput = element(by.id('field_valorTotal'));
    nomeSolicInput = element(by.id('field_nomeSolic'));
    sessaoCaixaSelect = element(by.id('field_sessaoCaixa'));
    contaSelect = element(by.id('field_conta'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setValorTotalInput(valorTotal) {
        await this.valorTotalInput.sendKeys(valorTotal);
    }

    async getValorTotalInput() {
        return this.valorTotalInput.getAttribute('value');
    }

    async setNomeSolicInput(nomeSolic) {
        await this.nomeSolicInput.sendKeys(nomeSolic);
    }

    async getNomeSolicInput() {
        return this.nomeSolicInput.getAttribute('value');
    }

    async sessaoCaixaSelectLastOption() {
        await this.sessaoCaixaSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async sessaoCaixaSelectOption(option) {
        await this.sessaoCaixaSelect.sendKeys(option);
    }

    getSessaoCaixaSelect(): ElementFinder {
        return this.sessaoCaixaSelect;
    }

    async getSessaoCaixaSelectedOption() {
        return this.sessaoCaixaSelect.element(by.css('option:checked')).getText();
    }

    async contaSelectLastOption() {
        await this.contaSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async contaSelectOption(option) {
        await this.contaSelect.sendKeys(option);
    }

    getContaSelect(): ElementFinder {
        return this.contaSelect;
    }

    async getContaSelectedOption() {
        return this.contaSelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class CompraDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-compra-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-compra'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
