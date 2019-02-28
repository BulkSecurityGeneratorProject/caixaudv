/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProdutoComponentsPage, ProdutoDeleteDialog, ProdutoUpdatePage } from './produto.page-object';

const expect = chai.expect;

describe('Produto e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let produtoUpdatePage: ProdutoUpdatePage;
    let produtoComponentsPage: ProdutoComponentsPage;
    let produtoDeleteDialog: ProdutoDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Produtos', async () => {
        await navBarPage.goToEntity('produto');
        produtoComponentsPage = new ProdutoComponentsPage();
        await browser.wait(ec.visibilityOf(produtoComponentsPage.title), 5000);
        expect(await produtoComponentsPage.getTitle()).to.eq('caixaudvApp.produto.home.title');
    });

    it('should load create Produto page', async () => {
        await produtoComponentsPage.clickOnCreateButton();
        produtoUpdatePage = new ProdutoUpdatePage();
        expect(await produtoUpdatePage.getPageTitle()).to.eq('caixaudvApp.produto.home.createOrEditLabel');
        await produtoUpdatePage.cancel();
    });

    it('should create and save Produtos', async () => {
        const nbButtonsBeforeCreate = await produtoComponentsPage.countDeleteButtons();

        await produtoComponentsPage.clickOnCreateButton();
        await promise.all([
            produtoUpdatePage.setNomeInput('nome'),
            produtoUpdatePage.setPrecoInput('5'),
            produtoUpdatePage.setDescricaoInput('descricao')
        ]);
        expect(await produtoUpdatePage.getNomeInput()).to.eq('nome');
        expect(await produtoUpdatePage.getPrecoInput()).to.eq('5');
        expect(await produtoUpdatePage.getDescricaoInput()).to.eq('descricao');
        await produtoUpdatePage.save();
        expect(await produtoUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await produtoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Produto', async () => {
        const nbButtonsBeforeDelete = await produtoComponentsPage.countDeleteButtons();
        await produtoComponentsPage.clickOnLastDeleteButton();

        produtoDeleteDialog = new ProdutoDeleteDialog();
        expect(await produtoDeleteDialog.getDialogTitle()).to.eq('caixaudvApp.produto.delete.question');
        await produtoDeleteDialog.clickOnConfirmButton();

        expect(await produtoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
