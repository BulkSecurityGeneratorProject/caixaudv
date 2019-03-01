package org.udv.nrc.caixaudv.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.udv.nrc.caixaudv.domain.Conta;
import org.udv.nrc.caixaudv.domain.ItemCompra;
import org.udv.nrc.caixaudv.domain.enumeration.NivelPermissao;
import org.udv.nrc.caixaudv.repository.ContaRepository;
import org.udv.nrc.caixaudv.repository.ItemCompraRepository;
import org.udv.nrc.caixaudv.security.UserAccountPermissionChecker;
import org.udv.nrc.caixaudv.web.rest.errors.BadRequestAlertException;
import org.udv.nrc.caixaudv.web.rest.errors.UserNotAuthorizedException;
import org.udv.nrc.caixaudv.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ItemCompra.
 */
@RestController
@RequestMapping("/api")
public class ItemCompraResource {

    private final Logger log = LoggerFactory.getLogger(ItemCompraResource.class);

    private static final String ENTITY_NAME = "itemCompra";

    private final ItemCompraRepository itemCompraRepository;

    @Autowired
    private ContaRepository contaRepository;

    private final List<NivelPermissao> canCRDAll = Arrays.asList(NivelPermissao.ADMIN, 
        NivelPermissao.OPERADOR);

    public ItemCompraResource(ItemCompraRepository itemCompraRepository) {
        this.itemCompraRepository = itemCompraRepository;
    }

    /**
     * POST  /item-compra : Create a new itemCompra.
     *
     * @param itemCompra the itemCompra to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itemCompra, or with status 400 (Bad Request) if the itemCompra has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/item-compras")
    public ResponseEntity<ItemCompra> createItemCompra(@Valid @RequestBody ItemCompra itemCompra) throws URISyntaxException {
        log.debug("REST request to save ItemCompra : {}", itemCompra);
        if (itemCompra.getId() != null) {
            throw new BadRequestAlertException("A new itemCompra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(UserAccountPermissionChecker.checkPermissao(contaTest, canCRDAll)){
            throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "not_authorized");
        }
        ItemCompra result = itemCompraRepository.save(itemCompra);
            return ResponseEntity.created(new URI("/api/item-compras/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /item-compra : Updates an existing itemCompra.
     *
     * @param itemCompra the itemCompra to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itemCompra,
     * or with status 400 (Bad Request) if the itemCompra is not valid,
     * or with status 500 (Internal Server Error) if the itemCompra couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/item-compra")
    public ResponseEntity<ItemCompra> updateItemCompra(@Valid @RequestBody ItemCompra itemCompra) throws URISyntaxException {
        log.debug("REST request to update ItemCompra : {}", itemCompra);
        if (itemCompra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(contaTest.getNivelPermissao().equals(NivelPermissao.CLIENTE)) {
            throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "not_authorized");
        }
        ItemCompra result = itemCompraRepository.save(itemCompra);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itemCompra.getId().toString()))
            .body(result);
    }

    /**
     * GET  /item-compra : get all the itemCompras.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of itemCompras in body
     */
    @GetMapping("/item-compras")
    public List<ItemCompra> getAllItemCompras() {
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        boolean isClient = contaTest.getNivelPermissao().equals(NivelPermissao.CLIENTE);
        if(isClient){
            return itemCompraRepository.findByUserIsCurrentUser();
        } 
        else if(!isClient && !UserAccountPermissionChecker.checkPermissao(contaTest, canCRDAll)){
            throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "not_authorized");
        }
        else return itemCompraRepository.findAll();
    }

    /**
     * GET  /item-compra/:id : get the "id" itemCompra.
     *
     * @param id the id of the itemCompra to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itemCompra, or with status 404 (Not Found)
     */
    @GetMapping("/item-compra/{id}")
    public ResponseEntity<ItemCompra> getItemCompra(@PathVariable Long id) {
        log.debug("REST request to get ItemCompra : {}", id);
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(!UserAccountPermissionChecker.checkPermissao(contaTest, canCRDAll)){
            throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "not_authorized");
        }
        Optional<ItemCompra> itemCompra = itemCompraRepository.findById(id);
        if(itemCompra.isPresent()){
            if(itemCompra.get().getCompra().getConta().equals(contaTest) || 
                    contaTest.getNivelPermissao().equals(NivelPermissao.ADMIN) ||
                    contaTest.getNivelPermissao().equals(NivelPermissao.OPERADOR))
                return ResponseEntity.ok(itemCompra.get());
            else throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "not_authorized");
        }
        else return ResponseEntity.notFound().build();
    }

    /**
     * DELETE  /item-compra/:id : delete the "id" itemCompra.
     *
     * @param id the id of the itemCompra to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/item-compra/{id}")
    public ResponseEntity<Void> deleteItemCompra(@PathVariable Long id) {
        log.debug("REST request to delete ItemCompra : {}", id);
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(!UserAccountPermissionChecker.checkPermissao(contaTest, canCRDAll)){
            throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "not_authorized");
        }
        itemCompraRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
