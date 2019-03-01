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
import org.udv.nrc.caixaudv.domain.Compra;
import org.udv.nrc.caixaudv.domain.Conta;
import org.udv.nrc.caixaudv.domain.enumeration.NivelPermissao;
import org.udv.nrc.caixaudv.repository.CompraRepository;
import org.udv.nrc.caixaudv.repository.ContaRepository;
import org.udv.nrc.caixaudv.security.UserAccountPermissionChecker;
import org.udv.nrc.caixaudv.web.rest.errors.BadRequestAlertException;
import org.udv.nrc.caixaudv.web.rest.errors.UserNotAuthorizedException;
import org.udv.nrc.caixaudv.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Compra.
 */
@RestController
@RequestMapping("/api")
public class CompraResource {

    private final Logger log = LoggerFactory.getLogger(CompraResource.class);

    private static final String ENTITY_NAME = "compra";

    private final CompraRepository compraRepository;

    @Autowired
    private ContaRepository contaRepository;

    private final List<NivelPermissao> canCRDAll = Arrays.asList(NivelPermissao.ADMIN, 
        NivelPermissao.OPERADOR);

    public CompraResource(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    /**
     * POST  /compras : Create a new compra.
     *
     * @param compra the compra to create
     * @return the ResponseEntity with status 201 (Created) and with body the new compra, or with status 400 (Bad Request) if the compra has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/compras")
    public ResponseEntity<Compra> createCompra(@Valid @RequestBody Compra compra) throws URISyntaxException {
        log.debug("REST request to save Compra : {}", compra);
        if (compra.getId() != null) {
            throw new BadRequestAlertException("A new compra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(!UserAccountPermissionChecker.checkPermissao(contaTest, canCRDAll)){
            throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "missing_permission");
        }
        Compra result = compraRepository.save(compra);
            return ResponseEntity.created(new URI("/api/compras/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /compras : Updates an existing compra.
     *
     * @param compra the compra to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated compra,
     * or with status 400 (Bad Request) if the compra is not valid,
     * or with status 500 (Internal Server Error) if the compra couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/compras")
    public ResponseEntity<Compra> updateCompra(@Valid @RequestBody Compra compra) throws URISyntaxException {
        log.debug("REST request to update Compra : {}", compra);
        if (compra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(!contaTest.getNivelPermissao().equals(NivelPermissao.ADMIN)) {
            throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "missing_permission");
        }
        Compra result = compraRepository.save(compra);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, compra.getId().toString()))
            .body(result);
    }

    /**
     * GET  /compras : get all the compras.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of compras in body
     */
    @GetMapping("/compras")
    public List<Compra> getAllCompras() {
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(!UserAccountPermissionChecker.checkPermissao(contaTest, canCRDAll)){
            return compraRepository.findByUserIsCurrentUser();
        }
        else return compraRepository.findAll();
    }

    /**
     * GET  /compras/:id : get the "id" compra.
     *
     * @param id the id of the compra to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the compra, or with status 404 (Not Found)
     */
    @GetMapping("/compras/{id}")
    public ResponseEntity<Compra> getCompra(@PathVariable Long id) {
        log.debug("REST request to get Compra : {}", id);
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        Optional<Compra> compra = compraRepository.findById(id);
        if(compra.isPresent()){
            if(compra.get().getConta().equals(contaTest) || 
                    contaTest.getNivelPermissao().equals(NivelPermissao.ADMIN) ||
                    contaTest.getNivelPermissao().equals(NivelPermissao.OPERADOR))
                return ResponseEntity.ok(compra.get());
            else throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "missing_permission");
        }
        else return ResponseEntity.notFound().build();
    }

    /**
     * DELETE  /compras/:id : delete the "id" compra.
     *
     * @param id the id of the compra to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/compras/{id}")
    public ResponseEntity<Void> deleteCompra(@PathVariable Long id) {
        log.debug("REST request to delete Compra : {}", id);
        Conta contaTest = contaRepository.findByUserIsCurrentUser();
        if(!UserAccountPermissionChecker.checkPermissao(contaTest, canCRDAll)){
            throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "missing_permission");
        }
        compraRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
