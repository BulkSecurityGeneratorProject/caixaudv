package org.udv.nrc.caixaudv.web.rest;
import org.udv.nrc.caixaudv.domain.Ressarcimento;
import org.udv.nrc.caixaudv.repository.RessarcimentoRepository;
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
 * REST controller for managing Ressarcimento.
 */
@RestController
@RequestMapping("/api")
public class RessarcimentoResource {

    private final Logger log = LoggerFactory.getLogger(RessarcimentoResource.class);

    private static final String ENTITY_NAME = "ressarcimento";

    private final RessarcimentoRepository ressarcimentoRepository;

    public RessarcimentoResource(RessarcimentoRepository ressarcimentoRepository) {
        this.ressarcimentoRepository = ressarcimentoRepository;
    }

    /**
     * POST  /ressarcimentos : Create a new ressarcimento.
     *
     * @param ressarcimento the ressarcimento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ressarcimento, or with status 400 (Bad Request) if the ressarcimento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ressarcimentos")
    public ResponseEntity<Ressarcimento> createRessarcimento(@Valid @RequestBody Ressarcimento ressarcimento) throws URISyntaxException {
        log.debug("REST request to save Ressarcimento : {}", ressarcimento);
        if (ressarcimento.getId() != null) {
            throw new BadRequestAlertException("A new ressarcimento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ressarcimento result = ressarcimentoRepository.save(ressarcimento);
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
    public ResponseEntity<Ressarcimento> updateRessarcimento(@Valid @RequestBody Ressarcimento ressarcimento) throws URISyntaxException {
        log.debug("REST request to update Ressarcimento : {}", ressarcimento);
        if (ressarcimento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ressarcimento result = ressarcimentoRepository.save(ressarcimento);
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
    public List<Ressarcimento> getAllRessarcimentos() {
        log.debug("REST request to get all Ressarcimentos");
        return ressarcimentoRepository.findAll();
    }

    /**
     * GET  /ressarcimentos/:id : get the "id" ressarcimento.
     *
     * @param id the id of the ressarcimento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ressarcimento, or with status 404 (Not Found)
     */
    @GetMapping("/ressarcimentos/{id}")
    public ResponseEntity<Ressarcimento> getRessarcimento(@PathVariable Long id) {
        log.debug("REST request to get Ressarcimento : {}", id);
        Optional<Ressarcimento> ressarcimento = ressarcimentoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ressarcimento);
    }

    /**
     * DELETE  /ressarcimentos/:id : delete the "id" ressarcimento.
     *
     * @param id the id of the ressarcimento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ressarcimentos/{id}")
    public ResponseEntity<Void> deleteRessarcimento(@PathVariable Long id) {
        log.debug("REST request to delete Ressarcimento : {}", id);
        ressarcimentoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
