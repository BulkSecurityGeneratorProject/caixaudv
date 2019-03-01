package org.udv.nrc.caixaudv.web.rest;
import org.udv.nrc.caixaudv.domain.Conta;
import org.udv.nrc.caixaudv.domain.Produto;
import org.udv.nrc.caixaudv.domain.enumeration.NivelPermissao;
import org.udv.nrc.caixaudv.repository.ContaRepository;
import org.udv.nrc.caixaudv.repository.ProdutoRepository;
import org.udv.nrc.caixaudv.security.UserAccountPermissionChecker;
import org.udv.nrc.caixaudv.web.rest.errors.BadRequestAlertException;
import org.udv.nrc.caixaudv.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Produto.
 */
@RestController
@RequestMapping("/api")
public class ProdutoResource {

    private final Logger log = LoggerFactory.getLogger(ProdutoResource.class);

    private static final String ENTITY_NAME = "produto";

    private final ProdutoRepository produtoRepository;

    @Autowired
    private ContaRepository contaRepository;

    private final List<NivelPermissao> canCRUDAll = Arrays.asList(NivelPermissao.ADMIN,
        NivelPermissao.OPERADOR);

    public ProdutoResource(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    /**
     * POST  /produtos : Create a new produto.
     *
     * @param produto the produto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new produto, or with status 400 (Bad Request) if the produto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/produtos")
    public ResponseEntity<Produto> createProduto(@Valid @RequestBody Produto produto) throws URISyntaxException {
        log.debug("REST request to save Produto : {}", produto);
        if (produto.getId() != null) {
            throw new BadRequestAlertException("A new produto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(!contaTest.getNivelPermissao().equals(NivelPermissao.ADMIN)){
            throw new BadRequestAlertException("Usuário não autorizado!", ENTITY_NAME, "not_authorized");
        }
        Produto result = produtoRepository.save(produto);
            return ResponseEntity.created(new URI("/api/produtos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /produtos : Updates an existing produto.
     *
     * @param produto the produto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated produto,
     * or with status 400 (Bad Request) if the produto is not valid,
     * or with status 500 (Internal Server Error) if the produto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/produtos")
    public ResponseEntity<Produto> updateProduto(@Valid @RequestBody Produto produto) throws URISyntaxException {
        log.debug("REST request to update Produto : {}", produto);
        if (produto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(!contaTest.getNivelPermissao().equals(NivelPermissao.ADMIN)){
            throw new BadRequestAlertException("Usuário não autorizado!", ENTITY_NAME, "not_authorized");
        }
        Produto result = produtoRepository.save(produto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, produto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /produtos : get all the produtos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of produtos in body
     */
    @GetMapping("/produtos")
    public List<Produto> getAllProdutos() {
        log.debug("REST request to get all Produtos");
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(UserAccountPermissionChecker.checkPermissao(contaTest, canCRUDAll)){
            throw new BadRequestAlertException("Usuário não autorizado!", ENTITY_NAME, "not_authorized");
        }
        return produtoRepository.findAll();
    }

    /**
     * GET  /produtos/:id : get the "id" produto.
     *
     * @param id the id of the produto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the produto, or with status 404 (Not Found)
     */
    @GetMapping("/produtos/{id}")
    public ResponseEntity<Produto> getProduto(@PathVariable Long id) {
        log.debug("REST request to get Produto : {}", id);
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(UserAccountPermissionChecker.checkPermissao(contaTest, canCRUDAll)){
            throw new BadRequestAlertException("Usuário não autorizado!", ENTITY_NAME, "not_authorized");
        }
        Optional<Produto> produto = produtoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(produto);
    }

    /**
     * DELETE  /produtos/:id : delete the "id" produto.
     *
     * @param id the id of the produto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/produtos/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        log.debug("REST request to delete Produto : {}", id);
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(!contaTest.getNivelPermissao().equals(NivelPermissao.ADMIN)){
            throw new BadRequestAlertException("Usuário não autorizado!", ENTITY_NAME, "not_authorized");
        }
        produtoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
