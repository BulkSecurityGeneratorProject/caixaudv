package org.udv.nrc.cantinadorei.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.udv.nrc.cantinadorei.domain.Ressarcimento;
import org.udv.nrc.cantinadorei.domain.SessaoCaixa;
import org.udv.nrc.cantinadorei.repository.ContaRepository;
import org.udv.nrc.cantinadorei.repository.RessarcimentoRepository;
import org.udv.nrc.cantinadorei.repository.SessaoCaixaRepository;
import org.udv.nrc.cantinadorei.security.AuthoritiesConstants;
import org.udv.nrc.cantinadorei.security.SecurityUtils;
import org.udv.nrc.cantinadorei.web.rest.errors.BadRequestAlertException;
import org.udv.nrc.cantinadorei.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Ressarcimento.
 */
@RestController
@RequestMapping("/api")
public class RessarcimentoResource {

    private final Logger log = LoggerFactory.getLogger(RessarcimentoResource.class);

    private static final String ENTITY_NAME = "ressarcimento";

    private final RessarcimentoRepository ressarcimentoRepository;

    private static List<String> canCRAll;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private SessaoCaixaRepository sessaoCaixaRepository;

    public RessarcimentoResource(RessarcimentoRepository ressarcimentoRepository) {
        this.ressarcimentoRepository = ressarcimentoRepository;
        canCRAll = Arrays.asList(AuthoritiesConstants.ADMIN, AuthoritiesConstants.OPERATOR,
            AuthoritiesConstants.DBA);
    }

    /**
     * POST  /ressarcimentos : Create a new ressarcimento.
     *
     * @param ressarcimento the ressarcimento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ressarcimento, or with status 400 (Bad Request) if the ressarcimento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ressarcimentos")
    @PreAuthorize("hasAnyRole('ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<Ressarcimento> createRessarcimento(@Valid @RequestBody Ressarcimento ressarcimento) throws URISyntaxException {
        log.debug("REST request to save Ressarcimento : {}", ressarcimento);
        if(!sessaoCaixaRepository.findOneByDate(LocalDate.now()).isPresent()) {
            throw new BadRequestAlertException("Não há sessão do caixa registrada para hoje", ENTITY_NAME, "noSessaoCaixa");
        }
        if (ressarcimento.getId() != null) {
            throw new BadRequestAlertException("A new ressarcimento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if(ressarcimento.getConta() == null) {
            throw new BadRequestAlertException("Apenas clientes podem ter ressarcimentos", ENTITY_NAME, "illegal_assignment");
        }

        //Add to conta the ressarcimento value 
        Ressarcimento result = updateRessarcimentoData(ressarcimento);
        
        return ResponseEntity.created(new URI("/api/ressarcimentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ressarcimentos : Updates an existing ressarcimento.
     *
     * @param ressarcimento the ressarcimento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ressarcimento,
     * or with status 400 (Bad Request) if the ressarcimento is not valid,
     * or with status 500 (Internal Server Error) if the ressarcimento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ressarcimentos")
    @PreAuthorize("hasAnyRole('ROLE_DBA', 'ROLE_ADMIN')")
    public ResponseEntity<Ressarcimento> updateRessarcimento(@Valid @RequestBody Ressarcimento ressarcimento) throws URISyntaxException {
        log.debug("REST request to update Ressarcimento : {}", ressarcimento);
        if (ressarcimento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if(ressarcimento.getConta() == null) {
            throw new BadRequestAlertException("Apenas clientes podem ter ressarcimentos", ENTITY_NAME, "illegalAssignment");
        }

        //Rollback ressarcimento and prepare to update it
        rollbackRessarcimentoData(ressarcimento.getId());
        Ressarcimento result = updateRessarcimentoData(ressarcimento);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ressarcimento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ressarcimentos : get all the ressarcimentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ressarcimentos in body
     */
    @GetMapping("/ressarcimentos")
    @PreAuthorize("hasAnyRole('ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CLIENT')")
    public List<Ressarcimento> getAllRessarcimentos() {
        log.debug("REST request to get all Ressarcimentos");
        if(!SecurityUtils.currentUserMatchesRole(canCRAll)){
            return ressarcimentoRepository.findByUserIsCurrentUser();
        }
        return ressarcimentoRepository.findAll();
    }

    /**
     * GET  /ressarcimentos/:id : get the "id" ressarcimento.
     *
     * @param id the id of the ressarcimento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ressarcimento, or with status 404 (Not Found)
     */
    @GetMapping("/ressarcimentos/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR', 'ROLE_CLIENT')")
    public ResponseEntity<Ressarcimento> getRessarcimento(@PathVariable Long id) {
        log.debug("REST request to get Ressarcimento : {}", id);
        Optional<Ressarcimento> ressarcimento = ressarcimentoRepository.findById(id);
        if(ressarcimento.isPresent()) {
            String currentUserLogin = SecurityUtils.getCurrentUserLogin().get();
            if(ressarcimento.get().getConta().getUser()
                    .getLogin().equals(currentUserLogin) ||
                    SecurityUtils.currentUserMatchesRole(canCRAll)) {
                return ResponseEntity.ok(ressarcimento.get());
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE  /ressarcimentos/:id : delete the "id" ressarcimento.
     *
     * @param id the id of the ressarcimento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ressarcimentos/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DBA', 'ROLE_ADMIN', 'ROLE_OPERATOR')")
    public ResponseEntity<Void> deleteRessarcimento(@PathVariable Long id) {
        log.debug("REST request to delete Ressarcimento : {}", id);
        
        //Rollback ressarcimento
        rollbackRessarcimentoData(id);

        ressarcimentoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * Given a ressarcimento bean, the point of
     * this method is to update the ressarcimento data
     * by subtracting ressarcimento's conta total value 
     * from the ressarcimentos's one
     */
    private Ressarcimento updateRessarcimentoData(Ressarcimento ressarcimento) {
        //trick to get the ressarcimento's current state 
        Conta contaToUpdate = contaRepository.findAll().stream()
            .filter(conta -> ressarcimento.getConta().getId().equals(conta.getId()))
            .collect(Collectors.toList()).get(0);
        contaToUpdate.setSaldoAtual(contaToUpdate.getSaldoAtual() + ressarcimento.getValor());
        contaRepository.save(contaToUpdate);  //save ressarcimento to conta

        //Update sessaoCaixa
        SessaoCaixa sessaoToUpdate = sessaoCaixaRepository.findAll().stream()
            .filter(sessao -> ressarcimento.getSessaoCaixa().getId().equals(sessao.getId()))
            .collect(Collectors.toList()).get(0);
        sessaoToUpdate.setValorAtual(sessaoToUpdate.getValorAtual() + ressarcimento.getValor());
        sessaoCaixaRepository.save(sessaoToUpdate);
        return ressarcimentoRepository.save(ressarcimento);
    }

    /**
     * Given a ressarcimento's Id, the point of 
     * this method is to rollback (undo) the ressarcimento
     * effects to ressarcimento's current user conta,
     * giving back the ressarcimento's total value to 
     * user conta's total balance.
     * 
     * In updateRessarcimento method, this method MUST 
     * preceed updateRessarcimentoData method, in order to 
     * rollback ressarcimento data an prepare to update it.
     * 
     * @see updateRessarcimento
     * @see updateRessarcimentoData
     */
    private void rollbackRessarcimentoData(Long ressarcimentoId) {
        //trick to avoid JPA session error
        Ressarcimento previousRessarcimento = ressarcimentoRepository.findAll().stream()
        .filter(ressarcimento -> ressarcimento.getId().equals(ressarcimentoId))
        .collect(Collectors.toList()).get(0); //same as get ressarcimento by id
        Conta contaToRollback = previousRessarcimento.getConta();
        contaToRollback.setSaldoAtual(contaToRollback.getSaldoAtual() - previousRessarcimento.getValor());
        contaRepository.save(contaToRollback);

        //Update sessaoCaixa
        SessaoCaixa sessaoToUpdate = previousRessarcimento.getSessaoCaixa();
        sessaoToUpdate.setValorAtual(sessaoToUpdate.getValorAtual() - previousRessarcimento.getValor());
        sessaoCaixaRepository.save(sessaoToUpdate);
    }
}
