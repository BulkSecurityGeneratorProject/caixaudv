import { element, by, ElementFinder } from 'protractor';

export class ItemCompraComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-item-compra div table .btn-danger'));
    title = element.all(by.css('jhi-item-compra div h2#page-heading span')).first();

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

export class ItemCompraUpdatePage {
    pageTitle = element(by.id('jhi-item-compra-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    quantInput = element(by.id('field_quant'));
    produtoSelect = element(by.id('field_produto'));
    compraSelect = element(by.id('field_compra'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setQuantInput(quant) {
        await this.quantInput.sendKeys(quant);
    }

    async getQuantInput() {
        return this.quantInput.getAttribute('value');
    }

    async produtoSelectLastOption() {
        await this.produtoSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async produtoSelectOption(option) {
        await this.produtoSelect.sendKeys(option);
    }

    getProdutoSelect(): ElementFinder {
        return this.produtoSelect;
    }

    async getProdutoSelectedOption() {
        return this.produtoSelect.element(by.css('option:checked')).getText();
    }

    async compraSelectLastOption() {
        await this.compraSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async compraSelectOption(option) {
        await this.compraSelect.sendKeys(option);
    }

    getCompraSelect(): ElementFinder {
        return this.compraSelect;
    }

    async getCompraSelectedOption() {
        return this.compraSelect.element(by.css('option:checked')).getText();
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

export class ItemCompraDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-itemCompra-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-itemCompra'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
