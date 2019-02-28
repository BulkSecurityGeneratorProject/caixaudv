/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ContaComponentsPage, ContaDeleteDialog, ContaUpdatePage } from './conta.page-object';

const expect = chai.expect;

describe('Conta e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let contaUpdatePage: ContaUpdatePage;
    let contaComponentsPage: ContaComponentsPage;
    let contaDeleteDialog: ContaDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Contas', async () => {
        await navBarPage.goToEntity('conta');
        contaComponentsPage = new ContaComponentsPage();
        await browser.wait(ec.visibilityOf(contaComponentsPage.title), 5000);
        expect(await contaComponentsPage.getTitle()).to.eq('caixaudvApp.conta.home.title');
    });

    it('should load create Conta page', async () => {
        await contaComponentsPage.clickOnCreateButton();
        contaUpdatePage = new ContaUpdatePage();
        expect(await contaUpdatePage.getPageTitle()).to.eq('caixaudvApp.conta.home.createOrEditLabel');
        await contaUpdatePage.cancel();
    });

    it('should create and save Contas', async () => {
        const nbButtonsBeforeCreate = await contaComponentsPage.countDeleteButtons();

        await contaComponentsPage.clickOnCreateButton();
        await promise.all([
            contaUpdatePage.setSaldoAtualInput('5'),
            contaUpdatePage.setDataAberturaInput('2000-12-31'),
            contaUpdatePage.nivelPermissaoSelectLastOption(),
            contaUpdatePage.userSelectLastOption()
        ]);
        expect(await contaUpdatePage.getSaldoAtualInput()).to.eq('5');
        expect(await contaUpdatePage.getDataAberturaInput()).to.eq('2000-12-31');
        await contaUpdatePage.save();
        expect(await contaUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await contaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Conta', async () => {
        const nbButtonsBeforeDelete = await contaComponentsPage.countDeleteButtons();
        await contaComponentsPage.clickOnLastDeleteButton();

        contaDeleteDialog = new ContaDeleteDialog();
        expect(await contaDeleteDialog.getDialogTitle()).to.eq('caixaudvApp.conta.delete.question');
        await contaDeleteDialog.clickOnConfirmButton();

        expect(await contaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
