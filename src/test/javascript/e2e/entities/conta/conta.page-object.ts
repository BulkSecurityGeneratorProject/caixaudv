import { element, by, ElementFinder } from 'protractor';

export class ContaComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-conta div table .btn-danger'));
    title = element.all(by.css('jhi-conta div h2#page-heading span')).first();

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

export class ContaUpdatePage {
    pageTitle = element(by.id('jhi-conta-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    saldoAtualInput = element(by.id('field_saldoAtual'));
    dataAberturaInput = element(by.id('field_dataAbertura'));
    userSelect = element(by.id('field_user'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setSaldoAtualInput(saldoAtual) {
        await this.saldoAtualInput.sendKeys(saldoAtual);
    }

    async getSaldoAtualInput() {
        return this.saldoAtualInput.getAttribute('value');
    }

    async setDataAberturaInput(dataAbertura) {
        await this.dataAberturaInput.sendKeys(dataAbertura);
    }

    async getDataAberturaInput() {
        return this.dataAberturaInput.getAttribute('value');
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

export class ContaDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-conta-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-conta'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
