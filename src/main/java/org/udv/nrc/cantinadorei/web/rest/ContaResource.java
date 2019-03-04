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
import org.udv.nrc.cantinadorei.domain.Conta;
import org.udv.nrc.cantinadorei.repository.ContaRepository;
import org.udv.nrc.cantinadorei.security.AuthoritiesConstants;
import org.udv.nrc.cantinadorei.security.SecurityUtils;
import org.udv.nrc.cantinadorei.service.UserService;
import org.udv.nrc.cantinadorei.web.rest.errors.BadRequestAlertException;
import org.udv.nrc.cantinadorei.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Conta.
 */
@RestController
@RequestMapping("/api")
public class ContaResource {

    private final Logger log = LoggerFactory.getLogger(ContaResource.class);

    private static final String ENTITY_NAME = "conta";

    private final ContaRepository contaRepository;

    private static List<String> canCRAll;

    @Autowired
    private UserService userService;

    public ContaResource(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
        canCRAll = Arrays.asList(AuthoritiesConstants.ADMIN, AuthoritiesConstants.OPERATOR,
            AuthoritiesConstants.DBA);
    }

    /**
     * POST  /contas : Create a new conta.
     *
     * @param conta the conta to create
     * @return the ResponseEntity with status 201 (Created) and with body the new conta, or with status 400 (Bad Request) if the conta has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contas")
    @PreAuthorize("hasAnyRole('ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<Conta> createConta(@Valid @RequestBody Conta conta) throws URISyntaxException {
        log.debug("REST request to save Conta : {}", conta);
        if (conta.getId() != null) {
            throw new BadRequestAlertException("A new conta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if(!userService.isUserInRole(conta.getUser().getLogin(), Arrays.asList(AuthoritiesConstants.CLIENT))) {
            throw new BadRequestAlertException("Apenas clientes podem ter uma conta do caixa", 
                ENTITY_NAME, "illegalAssignment");
        }
        Conta result = contaRepository.save(conta);
        return ResponseEntity.created(new URI("/api/contas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contas : Updates an existing conta.
     *
     * @param conta the conta to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated conta,
     * or with status 400 (Bad Request) if the conta is not valid,
     * or with status 500 (Internal Server Error) if the conta couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contas")
    @PreAuthorize("hasAnyRole('ROLE_DBA', 'ROLE_ADMIN')")
    public ResponseEntity<Conta> updateConta(@Valid @RequestBody Conta conta) throws URISyntaxException {
        log.debug("REST request to update Conta : {}", conta);
        if (conta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if(!userService.isUserInRole(conta.getUser().getLogin(), Arrays.asList(AuthoritiesConstants.CLIENT))) {
            throw new BadRequestAlertException("Apenas clientes podem ter uma conta", ENTITY_NAME, "illegalAssignment");
        }
        Conta result = contaRepository.save(conta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, conta.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contas : get all the contas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contas in body
     */
    @GetMapping("/contas")
    public List<Conta> getAllContas() {
        log.debug("REST request to get all Contas");
        if(!SecurityUtils.currentUserMatchesRole(canCRAll)){
            return contaRepository.findByUserIsCurrentUser();
        }
        return contaRepository.findAll();
    }

    /**
     * GET  /contas/:id : get the "id" conta.
     *
     * @param id the id of the conta to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the conta, or with status 404 (Not Found)
     */
    @GetMapping("/contas/{id}")
    public ResponseEntity<Conta> getConta(@PathVariable Long id) {
        log.debug("REST request to get Conta : {}", id);
        Optional<Conta> conta = contaRepository.findById(id);
        if(conta.isPresent()) {
            String currentUserLogin = SecurityUtils.getCurrentUserLogin().get();
            if(conta.get().getUser().getLogin().equals(currentUserLogin) ||
                    SecurityUtils.currentUserMatchesRole(canCRAll)) {
                return ResponseEntity.ok(conta.get());
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE  /contas/:id : delete the "id" conta.
     *
     * @param id the id of the conta to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contas/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DBA', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteConta(@PathVariable Long id) {
        log.debug("REST request to delete Conta : {}", id);
        contaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
