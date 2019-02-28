package org.udv.nrc.caixaudv.web.rest;
import org.udv.nrc.caixaudv.domain.ItemCompra;
import org.udv.nrc.caixaudv.repository.ItemCompraRepository;
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
 * REST controller for managing ItemCompra.
 */
@RestController
@RequestMapping("/api")
public class ItemCompraResource {

    private final Logger log = LoggerFactory.getLogger(ItemCompraResource.class);

    private static final String ENTITY_NAME = "itemCompra";

    private final ItemCompraRepository itemCompraRepository;

    public ItemCompraResource(ItemCompraRepository itemCompraRepository) {
        this.itemCompraRepository = itemCompraRepository;
    }

    /**
     * POST  /item-compras : Create a new itemCompra.
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
        ItemCompra result = itemCompraRepository.save(itemCompra);
        return ResponseEntity.created(new URI("/api/item-compras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /item-compras : Updates an existing itemCompra.
     *
     * @param itemCompra the itemCompra to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itemCompra,
     * or with status 400 (Bad Request) if the itemCompra is not valid,
     * or with status 500 (Internal Server Error) if the itemCompra couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/item-compras")
    public ResponseEntity<ItemCompra> updateItemCompra(@Valid @RequestBody ItemCompra itemCompra) throws URISyntaxException {
        log.debug("REST request to update ItemCompra : {}", itemCompra);
        if (itemCompra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemCompra result = itemCompraRepository.save(itemCompra);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itemCompra.getId().toString()))
            .body(result);
    }

    /**
     * GET  /item-compras : get all the itemCompras.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of itemCompras in body
     */
    @GetMapping("/item-compras")
    public List<ItemCompra> getAllItemCompras() {
        log.debug("REST request to get all ItemCompras");
        return itemCompraRepository.findAll();
    }

    /**
     * GET  /item-compras/:id : get the "id" itemCompra.
     *
     * @param id the id of the itemCompra to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itemCompra, or with status 404 (Not Found)
     */
    @GetMapping("/item-compras/{id}")
    public ResponseEntity<ItemCompra> getItemCompra(@PathVariable Long id) {
        log.debug("REST request to get ItemCompra : {}", id);
        Optional<ItemCompra> itemCompra = itemCompraRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(itemCompra);
    }

    /**
     * DELETE  /item-compras/:id : delete the "id" itemCompra.
     *
     * @param id the id of the itemCompra to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/item-compras/{id}")
    public ResponseEntity<Void> deleteItemCompra(@PathVariable Long id) {
        log.debug("REST request to delete ItemCompra : {}", id);
        itemCompraRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
