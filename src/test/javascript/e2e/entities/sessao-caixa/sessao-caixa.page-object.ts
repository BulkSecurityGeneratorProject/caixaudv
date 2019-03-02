import { element, by, ElementFinder } from 'protractor';

export class SessaoCaixaComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-sessao-caixa div table .btn-danger'));
    title = element.all(by.css('jhi-sessao-caixa div h2#page-heading span')).first();

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

export class SessaoCaixaUpdatePage {
    pageTitle = element(by.id('jhi-sessao-caixa-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    valorAtualInput = element(by.id('field_valorAtual'));
    dataInput = element(by.id('field_data'));
    userSelect = element(by.id('field_user'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setValorAtualInput(valorAtual) {
        await this.valorAtualInput.sendKeys(valorAtual);
    }

    async getValorAtualInput() {
        return this.valorAtualInput.getAttribute('value');
    }

    async setDataInput(data) {
        await this.dataInput.sendKeys(data);
    }

    async getDataInput() {
        return this.dataInput.getAttribute('value');
    }

    async userSelectLastOption() {
        await this.userSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async userSelectOption(option) {
        await this.userSelect.sendKeys(option);
    }

    getUserSelect(): ElementFinder {
        return this.userSelect;
    }

    async getUserSelectedOption() {
        return this.userSelect.element(by.css('option:checked')).getText();
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

export class SessaoCaixaDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-sessaoCaixa-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-sessaoCaixa'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
