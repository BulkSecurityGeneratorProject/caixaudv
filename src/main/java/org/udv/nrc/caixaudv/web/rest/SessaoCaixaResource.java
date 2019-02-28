package org.udv.nrc.caixaudv.web.rest;
import org.udv.nrc.caixaudv.domain.SessaoCaixa;
import org.udv.nrc.caixaudv.repository.SessaoCaixaRepository;
import org.udv.nrc.caixaudv.web.rest.errors.BadRequestAlertException;
import org.udv.nrc.caixaudv.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SessaoCaixa.
 */
@RestController
@RequestMapping("/api")
public class SessaoCaixaResource {

    private final Logger log = LoggerFactory.getLogger(SessaoCaixaResource.class);

    private static final String ENTITY_NAME = "sessaoCaixa";

    private final SessaoCaixaRepository sessaoCaixaRepository;

    public SessaoCaixaResource(SessaoCaixaRepository sessaoCaixaRepository) {
        this.sessaoCaixaRepository = sessaoCaixaRepository;
    }

    /**
     * POST  /sessao-caixas : Create a new sessaoCaixa.
     *
     * @param sessaoCaixa the sessaoCaixa to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sessaoCaixa, or with status 400 (Bad Request) if the sessaoCaixa has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sessao-caixas")
    public ResponseEntity<SessaoCaixa> createSessaoCaixa(@Valid @RequestBody SessaoCaixa sessaoCaixa) throws URISyntaxException {
        log.debug("REST request to save SessaoCaixa : {}", sessaoCaixa);
        if (sessaoCaixa.getId() != null) {
            throw new BadRequestAlertException("A new sessaoCaixa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SessaoCaixa result = sessaoCaixaRepository.save(sessaoCaixa);
        return ResponseEntity.created(new URI("/api/sessao-caixas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sessao-caixas : Updates an existing sessaoCaixa.
     *
     * @param sessaoCaixa the sessaoCaixa to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sessaoCaixa,
     * or with status 400 (Bad Request) if the sessaoCaixa is not valid,
     * or with status 500 (Internal Server Error) if the sessaoCaixa couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sessao-caixas")
    public ResponseEntity<SessaoCaixa> updateSessaoCaixa(@Valid @RequestBody SessaoCaixa sessaoCaixa) throws URISyntaxException {
        log.debug("REST request to update SessaoCaixa : {}", sessaoCaixa);
        if (sessaoCaixa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SessaoCaixa result = sessaoCaixaRepository.save(sessaoCaixa);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sessaoCaixa.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sessao-caixas : get all the sessaoCaixas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sessaoCaixas in body
     */
    @GetMapping("/sessao-caixas")
    public List<SessaoCaixa> getAllSessaoCaixas() {
        log.debug("REST request to get all SessaoCaixas");
        return sessaoCaixaRepository.findAll();
    }

    /**
     * GET  /sessao-caixas/:id : get the "id" sessaoCaixa.
     *
     * @param id the id of the sessaoCaixa to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sessaoCaixa, or with status 404 (Not Found)
     */
    @GetMapping("/sessao-caixas/{id}")
    public ResponseEntity<SessaoCaixa> getSessaoCaixa(@PathVariable Long id) {
        log.debug("REST request to get SessaoCaixa : {}", id);
        Optional<SessaoCaixa> sessaoCaixa = sessaoCaixaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sessaoCaixa);
    }

    /**
     * DELETE  /sessao-caixas/:id : delete the "id" sessaoCaixa.
     *
     * @param id the id of the sessaoCaixa to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sessao-caixas/{id}")
    public ResponseEntity<Void> deleteSessaoCaixa(@PathVariable Long id) {
        log.debug("REST request to delete SessaoCaixa : {}", id);
        sessaoCaixaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
