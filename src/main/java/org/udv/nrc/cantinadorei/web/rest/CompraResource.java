package org.udv.nrc.cantinadorei.web.rest;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.udv.nrc.cantinadorei.domain.Compra;
import org.udv.nrc.cantinadorei.domain.Conta;
import org.udv.nrc.cantinadorei.repository.CompraRepository;
import org.udv.nrc.cantinadorei.repository.ContaRepository;
import org.udv.nrc.cantinadorei.security.AuthoritiesConstants;
import org.udv.nrc.cantinadorei.security.SecurityUtils;
import org.udv.nrc.cantinadorei.service.UserService;
import org.udv.nrc.cantinadorei.web.rest.errors.BadRequestAlertException;
import org.udv.nrc.cantinadorei.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Compra.
 */
@RestController
@RequestMapping("/api")
public class CompraResource {

    private final Logger log = LoggerFactory.getLogger(CompraResource.class);

    private static final String ENTITY_NAME = "compra";

    private final CompraRepository compraRepository;
    
    private static List<String> canCRDAll;

    @Autowired
    private ContaRepository contaRepository;
    
    public CompraResource(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
        canCRDAll = Arrays.asList(AuthoritiesConstants.ADMIN, AuthoritiesConstants.OPERATOR,
            AuthoritiesConstants.DBA);
    }

    /**
     * POST  /compras : Create a new compra.
     *
     * @param compra the compra to create
     * @return the ResponseEntity with status 201 (Created) and with body the new compra, or with status 400 (Bad Request) if the compra has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/compras")
    @PreAuthorize("hasAnyRole('ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<Compra> createCompra(@Valid @RequestBody Compra compra) throws URISyntaxException {
        log.debug("REST request to save Compra : {}", compra);
        if (compra.getId() != null) {
            throw new BadRequestAlertException("A new compra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if(compra.getConta() == null){
            throw new BadRequestAlertException("Apenas clientes podem ter compras", ENTITY_NAME, "illegal_assignment");
        }
        Conta contaToUpdate = compra.getConta();
        contaToUpdate.setSaldoAtual(contaToUpdate.getSaldoAtual() - compra.getValorTotal());
        contaRepository.save(contaToUpdate);
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
    @PreAuthorize("hasAnyRole('ROLE_DBA', 'ROLE_ADMIN')")
    public ResponseEntity<Compra> updateCompra(@Valid @RequestBody Compra compra) throws URISyntaxException {
        log.debug("REST request to update Compra : {}", compra);
        if (compra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
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
    @PreAuthorize("hasAnyRole('ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CLIENT')")
    public List<Compra> getAllCompras() {
        log.debug("REST request to get all Compras");
        if(!SecurityUtils.currentUserMatchesRole(canCRDAll)){
            return compraRepository.findByUserIsCurrentUser();
        }
        return compraRepository.findAll();
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
        Optional<Compra> compra = compraRepository.findById(id);
        if(compra.isPresent()) {
            String currentUserLogin = SecurityUtils.getCurrentUserLogin().get();
            if(compra.get().getConta().getUser()
                    .getLogin().equals(currentUserLogin) ||
                    SecurityUtils.currentUserMatchesRole(canCRDAll)) {
                return ResponseEntity.ok(compra.get());
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE  /compras/:id : delete the "id" compra.
     *
     * @param id the id of the compra to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/compras/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<Void> deleteCompra(@PathVariable Long id) {
        log.debug("REST request to delete Compra : {}", id);        
        compraRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
