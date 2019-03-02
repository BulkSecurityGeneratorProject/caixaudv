/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SessaoCaixaComponentsPage, SessaoCaixaDeleteDialog, SessaoCaixaUpdatePage } from './sessao-caixa.page-object';

const expect = chai.expect;

describe('SessaoCaixa e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let sessaoCaixaUpdatePage: SessaoCaixaUpdatePage;
    let sessaoCaixaComponentsPage: SessaoCaixaComponentsPage;
    let sessaoCaixaDeleteDialog: SessaoCaixaDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load SessaoCaixas', async () => {
        await navBarPage.goToEntity('sessao-caixa');
        sessaoCaixaComponentsPage = new SessaoCaixaComponentsPage();
        await browser.wait(ec.visibilityOf(sessaoCaixaComponentsPage.title), 5000);
        expect(await sessaoCaixaComponentsPage.getTitle()).to.eq('cantinadoreiApp.sessaoCaixa.home.title');
    });

    it('should load create SessaoCaixa page', async () => {
        await sessaoCaixaComponentsPage.clickOnCreateButton();
        sessaoCaixaUpdatePage = new SessaoCaixaUpdatePage();
        expect(await sessaoCaixaUpdatePage.getPageTitle()).to.eq('cantinadoreiApp.sessaoCaixa.home.createOrEditLabel');
        await sessaoCaixaUpdatePage.cancel();
    });

    it('should create and save SessaoCaixas', async () => {
        const nbButtonsBeforeCreate = await sessaoCaixaComponentsPage.countDeleteButtons();

        await sessaoCaixaComponentsPage.clickOnCreateButton();
        await promise.all([
            sessaoCaixaUpdatePage.setValorAtualInput('5'),
            sessaoCaixaUpdatePage.setDataInput('2000-12-31'),
            sessaoCaixaUpdatePage.userSelectLastOption()
        ]);
        expect(await sessaoCaixaUpdatePage.getValorAtualInput()).to.eq('5');
        expect(await sessaoCaixaUpdatePage.getDataInput()).to.eq('2000-12-31');
        await sessaoCaixaUpdatePage.save();
        expect(await sessaoCaixaUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await sessaoCaixaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last SessaoCaixa', async () => {
        const nbButtonsBeforeDelete = await sessaoCaixaComponentsPage.countDeleteButtons();
        await sessaoCaixaComponentsPage.clickOnLastDeleteButton();

        sessaoCaixaDeleteDialog = new SessaoCaixaDeleteDialog();
        expect(await sessaoCaixaDeleteDialog.getDialogTitle()).to.eq('cantinadoreiApp.sessaoCaixa.delete.question');
        await sessaoCaixaDeleteDialog.clickOnConfirmButton();

        expect(await sessaoCaixaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
