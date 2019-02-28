package org.udv.nrc.caixaudv.web.rest;

import org.udv.nrc.caixaudv.CaixaudvApp;

import org.udv.nrc.caixaudv.domain.ItemCompra;
import org.udv.nrc.caixaudv.repository.ItemCompraRepository;
import org.udv.nrc.caixaudv.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static org.udv.nrc.caixaudv.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ItemCompraResource REST controller.
 *
 * @see ItemCompraResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaixaudvApp.class)
public class ItemCompraResourceIntTest {

    private static final Integer DEFAULT_QUANT = 1;
    private static final Integer UPDATED_QUANT = 2;

    @Autowired
    private ItemCompraRepository itemCompraRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restItemCompraMockMvc;

    private ItemCompra itemCompra;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemCompraResource itemCompraResource = new ItemCompraResource(itemCompraRepository);
        this.restItemCompraMockMvc = MockMvcBuilders.standaloneSetup(itemCompraResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemCompra createEntity(EntityManager em) {
        ItemCompra itemCompra = new ItemCompra()
            .quant(DEFAULT_QUANT);
        return itemCompra;
    }

    @Before
    public void initTest() {
        itemCompra = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemCompra() throws Exception {
        int databaseSizeBeforeCreate = itemCompraRepository.findAll().size();

        // Create the ItemCompra
        restItemCompraMockMvc.perform(post("/api/item-compras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCompra)))
            .andExpect(status().isCreated());

        // Validate the ItemCompra in the database
        List<ItemCompra> itemCompraList = itemCompraRepository.findAll();
        assertThat(itemCompraList).hasSize(databaseSizeBeforeCreate + 1);
        ItemCompra testItemCompra = itemCompraList.get(itemCompraList.size() - 1);
        assertThat(testItemCompra.getQuant()).isEqualTo(DEFAULT_QUANT);
    }

    @Test
    @Transactional
    public void createItemCompraWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemCompraRepository.findAll().size();

        // Create the ItemCompra with an existing ID
        itemCompra.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemCompraMockMvc.perform(post("/api/item-compras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCompra)))
            .andExpect(status().isBadRequest());

        // Validate the ItemCompra in the database
        List<ItemCompra> itemCompraList = itemCompraRepository.findAll();
        assertThat(itemCompraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuantIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCompraRepository.findAll().size();
        // set the field null
        itemCompra.setQuant(null);

        // Create the ItemCompra, which fails.

        restItemCompraMockMvc.perform(post("/api/item-compras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCompra)))
            .andExpect(status().isBadRequest());

        List<ItemCompra> itemCompraList = itemCompraRepository.findAll();
        assertThat(itemCompraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemCompras() throws Exception {
        // Initialize the database
        itemCompraRepository.saveAndFlush(itemCompra);

        // Get all the itemCompraList
        restItemCompraMockMvc.perform(get("/api/item-compras?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemCompra.getId().intValue())))
            .andExpect(jsonPath("$.[*].quant").value(hasItem(DEFAULT_QUANT)));
    }
    
    @Test
    @Transactional
    public void getItemCompra() throws Exception {
        // Initialize the database
        itemCompraRepository.saveAndFlush(itemCompra);

        // Get the itemCompra
        restItemCompraMockMvc.perform(get("/api/item-compras/{id}", itemCompra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemCompra.getId().intValue()))
            .andExpect(jsonPath("$.quant").value(DEFAULT_QUANT));
    }

    @Test
    @Transactional
    public void getNonExistingItemCompra() throws Exception {
        // Get the itemCompra
        restItemCompraMockMvc.perform(get("/api/item-compras/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemCompra() throws Exception {
        // Initialize the database
        itemCompraRepository.saveAndFlush(itemCompra);

        int databaseSizeBeforeUpdate = itemCompraRepository.findAll().size();

        // Update the itemCompra
        ItemCompra updatedItemCompra = itemCompraRepository.findById(itemCompra.getId()).get();
        // Disconnect from session so that the updates on updatedItemCompra are not directly saved in db
        em.detach(updatedItemCompra);
        updatedItemCompra
            .quant(UPDATED_QUANT);

        restItemCompraMockMvc.perform(put("/api/item-compras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItemCompra)))
            .andExpect(status().isOk());

        // Validate the ItemCompra in the database
        List<ItemCompra> itemCompraList = itemCompraRepository.findAll();
        assertThat(itemCompraList).hasSize(databaseSizeBeforeUpdate);
        ItemCompra testItemCompra = itemCompraList.get(itemCompraList.size() - 1);
        assertThat(testItemCompra.getQuant()).isEqualTo(UPDATED_QUANT);
    }

    @Test
    @Transactional
    public void updateNonExistingItemCompra() throws Exception {
        int databaseSizeBeforeUpdate = itemCompraRepository.findAll().size();

        // Create the ItemCompra

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemCompraMockMvc.perform(put("/api/item-compras")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCompra)))
            .andExpect(status().isBadRequest());

        // Validate the ItemCompra in the database
        List<ItemCompra> itemCompraList = itemCompraRepository.findAll();
        assertThat(itemCompraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteItemCompra() throws Exception {
        // Initialize the database
        itemCompraRepository.saveAndFlush(itemCompra);

        int databaseSizeBeforeDelete = itemCompraRepository.findAll().size();

        // Delete the itemCompra
        restItemCompraMockMvc.perform(delete("/api/item-compras/{id}", itemCompra.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ItemCompra> itemCompraList = itemCompraRepository.findAll();
        assertThat(itemCompraList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemCompra.class);
        ItemCompra itemCompra1 = new ItemCompra();
        itemCompra1.setId(1L);
        ItemCompra itemCompra2 = new ItemCompra();
        itemCompra2.setId(itemCompra1.getId());
        assertThat(itemCompra1).isEqualTo(itemCompra2);
        itemCompra2.setId(2L);
        assertThat(itemCompra1).isNotEqualTo(itemCompra2);
        itemCompra1.setId(null);
        assertThat(itemCompra1).isNotEqualTo(itemCompra2);
    }
}
