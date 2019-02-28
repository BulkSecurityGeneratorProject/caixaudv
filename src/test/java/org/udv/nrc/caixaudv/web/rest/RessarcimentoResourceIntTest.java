package org.udv.nrc.caixaudv.web.rest;

import org.udv.nrc.caixaudv.CaixaudvApp;

import org.udv.nrc.caixaudv.domain.Ressarcimento;
import org.udv.nrc.caixaudv.repository.RessarcimentoRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static org.udv.nrc.caixaudv.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RessarcimentoResource REST controller.
 *
 * @see RessarcimentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaixaudvApp.class)
public class RessarcimentoResourceIntTest {

    private static final Float DEFAULT_VALOR = 0F;
    private static final Float UPDATED_VALOR = 1F;

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private RessarcimentoRepository ressarcimentoRepository;

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

    private MockMvc restRessarcimentoMockMvc;

    private Ressarcimento ressarcimento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RessarcimentoResource ressarcimentoResource = new RessarcimentoResource(ressarcimentoRepository);
        this.restRessarcimentoMockMvc = MockMvcBuilders.standaloneSetup(ressarcimentoResource)
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
    public static Ressarcimento createEntity(EntityManager em) {
        Ressarcimento ressarcimento = new Ressarcimento()
            .valor(DEFAULT_VALOR)
            .data(DEFAULT_DATA);
        return ressarcimento;
    }

    @Before
    public void initTest() {
        ressarcimento = createEntity(em);
    }

    @Test
    @Transactional
    public void createRessarcimento() throws Exception {
        int databaseSizeBeforeCreate = ressarcimentoRepository.findAll().size();

        // Create the Ressarcimento
        restRessarcimentoMockMvc.perform(post("/api/ressarcimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ressarcimento)))
            .andExpect(status().isCreated());

        // Validate the Ressarcimento in the database
        List<Ressarcimento> ressarcimentoList = ressarcimentoRepository.findAll();
        assertThat(ressarcimentoList).hasSize(databaseSizeBeforeCreate + 1);
        Ressarcimento testRessarcimento = ressarcimentoList.get(ressarcimentoList.size() - 1);
        assertThat(testRessarcimento.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testRessarcimento.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    public void createRessarcimentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ressarcimentoRepository.findAll().size();

        // Create the Ressarcimento with an existing ID
        ressarcimento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRessarcimentoMockMvc.perform(post("/api/ressarcimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ressarcimento)))
            .andExpect(status().isBadRequest());

        // Validate the Ressarcimento in the database
        List<Ressarcimento> ressarcimentoList = ressarcimentoRepository.findAll();
        assertThat(ressarcimentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = ressarcimentoRepository.findAll().size();
        // set the field null
        ressarcimento.setValor(null);

        // Create the Ressarcimento, which fails.

        restRessarcimentoMockMvc.perform(post("/api/ressarcimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ressarcimento)))
            .andExpect(status().isBadRequest());

        List<Ressarcimento> ressarcimentoList = ressarcimentoRepository.findAll();
        assertThat(ressarcimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = ressarcimentoRepository.findAll().size();
        // set the field null
        ressarcimento.setData(null);

        // Create the Ressarcimento, which fails.

        restRessarcimentoMockMvc.perform(post("/api/ressarcimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ressarcimento)))
            .andExpect(status().isBadRequest());

        List<Ressarcimento> ressarcimentoList = ressarcimentoRepository.findAll();
        assertThat(ressarcimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRessarcimentos() throws Exception {
        // Initialize the database
        ressarcimentoRepository.saveAndFlush(ressarcimento);

        // Get all the ressarcimentoList
        restRessarcimentoMockMvc.perform(get("/api/ressarcimentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ressarcimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }
    
    @Test
    @Transactional
    public void getRessarcimento() throws Exception {
        // Initialize the database
        ressarcimentoRepository.saveAndFlush(ressarcimento);

        // Get the ressarcimento
        restRessarcimentoMockMvc.perform(get("/api/ressarcimentos/{id}", ressarcimento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ressarcimento.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRessarcimento() throws Exception {
        // Get the ressarcimento
        restRessarcimentoMockMvc.perform(get("/api/ressarcimentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRessarcimento() throws Exception {
        // Initialize the database
        ressarcimentoRepository.saveAndFlush(ressarcimento);

        int databaseSizeBeforeUpdate = ressarcimentoRepository.findAll().size();

        // Update the ressarcimento
        Ressarcimento updatedRessarcimento = ressarcimentoRepository.findById(ressarcimento.getId()).get();
        // Disconnect from session so that the updates on updatedRessarcimento are not directly saved in db
        em.detach(updatedRessarcimento);
        updatedRessarcimento
            .valor(UPDATED_VALOR)
            .data(UPDATED_DATA);

        restRessarcimentoMockMvc.perform(put("/api/ressarcimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRessarcimento)))
            .andExpect(status().isOk());

        // Validate the Ressarcimento in the database
        List<Ressarcimento> ressarcimentoList = ressarcimentoRepository.findAll();
        assertThat(ressarcimentoList).hasSize(databaseSizeBeforeUpdate);
        Ressarcimento testRessarcimento = ressarcimentoList.get(ressarcimentoList.size() - 1);
        assertThat(testRessarcimento.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testRessarcimento.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    public void updateNonExistingRessarcimento() throws Exception {
        int databaseSizeBeforeUpdate = ressarcimentoRepository.findAll().size();

        // Create the Ressarcimento

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRessarcimentoMockMvc.perform(put("/api/ressarcimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ressarcimento)))
            .andExpect(status().isBadRequest());

        // Validate the Ressarcimento in the database
        List<Ressarcimento> ressarcimentoList = ressarcimentoRepository.findAll();
        assertThat(ressarcimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRessarcimento() throws Exception {
        // Initialize the database
        ressarcimentoRepository.saveAndFlush(ressarcimento);

        int databaseSizeBeforeDelete = ressarcimentoRepository.findAll().size();

        // Delete the ressarcimento
        restRessarcimentoMockMvc.perform(delete("/api/ressarcimentos/{id}", ressarcimento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ressarcimento> ressarcimentoList = ressarcimentoRepository.findAll();
        assertThat(ressarcimentoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ressarcimento.class);
        Ressarcimento ressarcimento1 = new Ressarcimento();
        ressarcimento1.setId(1L);
        Ressarcimento ressarcimento2 = new Ressarcimento();
        ressarcimento2.setId(ressarcimento1.getId());
        assertThat(ressarcimento1).isEqualTo(ressarcimento2);
        ressarcimento2.setId(2L);
        assertThat(ressarcimento1).isNotEqualTo(ressarcimento2);
        ressarcimento1.setId(null);
        assertThat(ressarcimento1).isNotEqualTo(ressarcimento2);
    }
}
