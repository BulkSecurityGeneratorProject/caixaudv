package org.udv.nrc.caixaudv.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.udv.nrc.caixaudv.domain.enumeration.NivelPermissao;
import org.udv.nrc.caixaudv.repository.ContaRepository;
import org.udv.nrc.caixaudv.security.UserAccountPermissionChecker;
import org.udv.nrc.caixaudv.web.rest.errors.BadRequestAlertException;
import org.udv.nrc.caixaudv.web.rest.errors.UserNotAuthorizedException;
import org.udv.nrc.caixaudv.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Conta.
 */
@RestController
@RequestMapping("/api")
public class ContaResource {

    private final Logger log = LoggerFactory.getLogger(ContaResource.class);

    private static final String ENTITY_NAME = "conta";

    private final ContaRepository contaRepository;

    private final String ROLE_ADMIN = "ROLE_ADMIN";

    private final List<NivelPermissao> canCRAll = Arrays.asList(NivelPermissao.ADMIN, 
        NivelPermissao.OPERADOR);

    public ContaResource(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    /**
     * POST  /contas : Create a new conta.
     *
     * @param newConta the conta to create
     * @return the ResponseEntity with status 201 (Created) and with body the new conta, or with status 400 (Bad Request) if the conta has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contas")
    public ResponseEntity<Conta> createConta(@Valid @RequestBody Conta newConta, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Conta : {}", newConta);
        if (newConta.getId() != null) {
            throw new BadRequestAlertException("A new conta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conta currentConta = contaRepository.findByUserIsCurrentUser();
        if(!UserAccountPermissionChecker.checkPermissao(currentConta, canCRAll)){
            throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "missing_permission");
        }
        if((!newConta.getNivelPermissao().equals(NivelPermissao.CLIENTE) &&  //(New account is not to CLIENT &&
                !currentConta.getNivelPermissao().equals(NivelPermissao.ADMIN)) ||  //Usuário atual não é ADMIN) ||
                request.isUserInRole(ROLE_ADMIN)) {  //Prevent to assign permission to admin-role users
            throw new BadRequestAlertException("Apenas conta CLIENTE é permitido criar para usuários não-administrativos", 
                ENTITY_NAME, "illegal_account_creating");
        }
        Conta result = contaRepository.save(newConta);
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
    public ResponseEntity<Conta> updateConta(@Valid @RequestBody Conta conta) throws URISyntaxException {
        log.debug("REST request to update Conta : {}", conta);
        if (conta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Conta currentConta = contaRepository.findByUserIsCurrentUser();
        if(!currentConta.getNivelPermissao().equals(NivelPermissao.ADMIN)){
            throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "missing_permission");
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
        Conta currentConta = contaRepository.findByUserIsCurrentUser();
        if(!UserAccountPermissionChecker.checkPermissao(currentConta, canCRAll)){
            return Arrays.asList(currentConta);
        }
        else return contaRepository.findAll();
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
        Conta currentConta = contaRepository.findByUserIsCurrentUser();
        if(conta.isPresent()) {
            if(conta.get().equals(currentConta) || 
                    currentConta.getNivelPermissao().equals(NivelPermissao.ADMIN) ||
                    currentConta.getNivelPermissao().equals(NivelPermissao.OPERADOR))
                return ResponseEntity.ok(conta.get());
            else throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "missing_permission");
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
    public ResponseEntity<Void> deleteConta(@PathVariable Long id) {
        log.debug("REST request to delete Conta : {}", id);
        Conta currentConta = contaRepository.findByUserIsCurrentUser();
        if(!currentConta.getNivelPermissao().equals(NivelPermissao.ADMIN)){
            throw new UserNotAuthorizedException("Usuário não autorizado!", ENTITY_NAME, "missing_permission");
        }
        contaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
