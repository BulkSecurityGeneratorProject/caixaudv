/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ItemCompraComponentsPage, ItemCompraDeleteDialog, ItemCompraUpdatePage } from './item-compra.page-object';

const expect = chai.expect;

describe('ItemCompra e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let itemCompraUpdatePage: ItemCompraUpdatePage;
    let itemCompraComponentsPage: ItemCompraComponentsPage;
    let itemCompraDeleteDialog: ItemCompraDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load ItemCompras', async () => {
        await navBarPage.goToEntity('item-compra');
        itemCompraComponentsPage = new ItemCompraComponentsPage();
        await browser.wait(ec.visibilityOf(itemCompraComponentsPage.title), 5000);
        expect(await itemCompraComponentsPage.getTitle()).to.eq('caixaudvApp.itemCompra.home.title');
    });

    it('should load create ItemCompra page', async () => {
        await itemCompraComponentsPage.clickOnCreateButton();
        itemCompraUpdatePage = new ItemCompraUpdatePage();
        expect(await itemCompraUpdatePage.getPageTitle()).to.eq('caixaudvApp.itemCompra.home.createOrEditLabel');
        await itemCompraUpdatePage.cancel();
    });

    it('should create and save ItemCompras', async () => {
        const nbButtonsBeforeCreate = await itemCompraComponentsPage.countDeleteButtons();

        await itemCompraComponentsPage.clickOnCreateButton();
        await promise.all([
            itemCompraUpdatePage.setQuantInput('5'),
            itemCompraUpdatePage.produtoSelectLastOption(),
            itemCompraUpdatePage.compraSelectLastOption()
        ]);
        expect(await itemCompraUpdatePage.getQuantInput()).to.eq('5');
        await itemCompraUpdatePage.save();
        expect(await itemCompraUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await itemCompraComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last ItemCompra', async () => {
        const nbButtonsBeforeDelete = await itemCompraComponentsPage.countDeleteButtons();
        await itemCompraComponentsPage.clickOnLastDeleteButton();

        itemCompraDeleteDialog = new ItemCompraDeleteDialog();
        expect(await itemCompraDeleteDialog.getDialogTitle()).to.eq('caixaudvApp.itemCompra.delete.question');
        await itemCompraDeleteDialog.clickOnConfirmButton();

        expect(await itemCompraComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
