/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { RessarcimentoComponentsPage, RessarcimentoDeleteDialog, RessarcimentoUpdatePage } from './ressarcimento.page-object';

const expect = chai.expect;

describe('Ressarcimento e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let ressarcimentoUpdatePage: RessarcimentoUpdatePage;
    let ressarcimentoComponentsPage: RessarcimentoComponentsPage;
    let ressarcimentoDeleteDialog: RessarcimentoDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Ressarcimentos', async () => {
        await navBarPage.goToEntity('ressarcimento');
        ressarcimentoComponentsPage = new RessarcimentoComponentsPage();
        await browser.wait(ec.visibilityOf(ressarcimentoComponentsPage.title), 5000);
        expect(await ressarcimentoComponentsPage.getTitle()).to.eq('cantinadoreiApp.ressarcimento.home.title');
    });

    it('should load create Ressarcimento page', async () => {
        await ressarcimentoComponentsPage.clickOnCreateButton();
        ressarcimentoUpdatePage = new RessarcimentoUpdatePage();
        expect(await ressarcimentoUpdatePage.getPageTitle()).to.eq('cantinadoreiApp.ressarcimento.home.createOrEditLabel');
        await ressarcimentoUpdatePage.cancel();
    });

    it('should create and save Ressarcimentos', async () => {
        const nbButtonsBeforeCreate = await ressarcimentoComponentsPage.countDeleteButtons();

        await ressarcimentoComponentsPage.clickOnCreateButton();
        await promise.all([
            ressarcimentoUpdatePage.setValorInput('5'),
            ressarcimentoUpdatePage.setDataInput('2000-12-31'),
            ressarcimentoUpdatePage.sessaoCaixaSelectLastOption(),
            ressarcimentoUpdatePage.contaSelectLastOption()
        ]);
        expect(await ressarcimentoUpdatePage.getValorInput()).to.eq('5');
        expect(await ressarcimentoUpdatePage.getDataInput()).to.eq('2000-12-31');
        await ressarcimentoUpdatePage.save();
        expect(await ressarcimentoUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await ressarcimentoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Ressarcimento', async () => {
        const nbButtonsBeforeDelete = await ressarcimentoComponentsPage.countDeleteButtons();
        await ressarcimentoComponentsPage.clickOnLastDeleteButton();

        ressarcimentoDeleteDialog = new RessarcimentoDeleteDialog();
        expect(await ressarcimentoDeleteDialog.getDialogTitle()).to.eq('cantinadoreiApp.ressarcimento.delete.question');
        await ressarcimentoDeleteDialog.clickOnConfirmButton();

        expect(await ressarcimentoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
