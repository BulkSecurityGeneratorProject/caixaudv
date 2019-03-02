/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CompraComponentsPage, CompraDeleteDialog, CompraUpdatePage } from './compra.page-object';

const expect = chai.expect;

describe('Compra e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let compraUpdatePage: CompraUpdatePage;
    let compraComponentsPage: CompraComponentsPage;
    let compraDeleteDialog: CompraDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Compras', async () => {
        await navBarPage.goToEntity('compra');
        compraComponentsPage = new CompraComponentsPage();
        await browser.wait(ec.visibilityOf(compraComponentsPage.title), 5000);
        expect(await compraComponentsPage.getTitle()).to.eq('cantinadoreiApp.compra.home.title');
    });

    it('should load create Compra page', async () => {
        await compraComponentsPage.clickOnCreateButton();
        compraUpdatePage = new CompraUpdatePage();
        expect(await compraUpdatePage.getPageTitle()).to.eq('cantinadoreiApp.compra.home.createOrEditLabel');
        await compraUpdatePage.cancel();
    });

    it('should create and save Compras', async () => {
        const nbButtonsBeforeCreate = await compraComponentsPage.countDeleteButtons();

        await compraComponentsPage.clickOnCreateButton();
        await promise.all([
            compraUpdatePage.setValorTotalInput('5'),
            compraUpdatePage.setNomeSolicInput('nomeSolic'),
            compraUpdatePage.sessaoCaixaSelectLastOption(),
            compraUpdatePage.contaSelectLastOption()
        ]);
        expect(await compraUpdatePage.getValorTotalInput()).to.eq('5');
        expect(await compraUpdatePage.getNomeSolicInput()).to.eq('nomeSolic');
        await compraUpdatePage.save();
        expect(await compraUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await compraComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Compra', async () => {
        const nbButtonsBeforeDelete = await compraComponentsPage.countDeleteButtons();
        await compraComponentsPage.clickOnLastDeleteButton();

        compraDeleteDialog = new CompraDeleteDialog();
        expect(await compraDeleteDialog.getDialogTitle()).to.eq('cantinadoreiApp.compra.delete.question');
        await compraDeleteDialog.clickOnConfirmButton();

        expect(await compraComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
