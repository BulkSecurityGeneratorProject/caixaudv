package org.udv.nrc.caixaudv.web.rest;

import org.udv.nrc.caixaudv.CaixaudvApp;

import org.udv.nrc.caixaudv.domain.SessaoCaixa;
import org.udv.nrc.caixaudv.repository.SessaoCaixaRepository;
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
 * Test class for the SessaoCaixaResource REST controller.
 *
 * @see SessaoCaixaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaixaudvApp.class)
public class SessaoCaixaResourceIntTest {

    private static final Float DEFAULT_VALOR_ATUAL = 0F;
    private static final Float UPDATED_VALOR_ATUAL = 1F;

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SessaoCaixaRepository sessaoCaixaRepository;

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

    private MockMvc restSessaoCaixaMockMvc;

    private SessaoCaixa sessaoCaixa;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SessaoCaixaResource sessaoCaixaResource = new SessaoCaixaResource(sessaoCaixaRepository);
        this.restSessaoCaixaMockMvc = MockMvcBuilders.standaloneSetup(sessaoCaixaResource)
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
    public static SessaoCaixa createEntity(EntityManager em) {
        SessaoCaixa sessaoCaixa = new SessaoCaixa()
            .valorAtual(DEFAULT_VALOR_ATUAL)
            .data(DEFAULT_DATA);
        return sessaoCaixa;
    }

    @Before
    public void initTest() {
        sessaoCaixa = createEntity(em);
    }

    @Test
    @Transactional
    public void createSessaoCaixa() throws Exception {
        int databaseSizeBeforeCreate = sessaoCaixaRepository.findAll().size();

        // Create the SessaoCaixa
        restSessaoCaixaMockMvc.perform(post("/api/sessao-caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessaoCaixa)))
            .andExpect(status().isCreated());

        // Validate the SessaoCaixa in the database
        List<SessaoCaixa> sessaoCaixaList = sessaoCaixaRepository.findAll();
        assertThat(sessaoCaixaList).hasSize(databaseSizeBeforeCreate + 1);
        SessaoCaixa testSessaoCaixa = sessaoCaixaList.get(sessaoCaixaList.size() - 1);
        assertThat(testSessaoCaixa.getValorAtual()).isEqualTo(DEFAULT_VALOR_ATUAL);
        assertThat(testSessaoCaixa.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    public void createSessaoCaixaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sessaoCaixaRepository.findAll().size();

        // Create the SessaoCaixa with an existing ID
        sessaoCaixa.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessaoCaixaMockMvc.perform(post("/api/sessao-caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessaoCaixa)))
            .andExpect(status().isBadRequest());

        // Validate the SessaoCaixa in the database
        List<SessaoCaixa> sessaoCaixaList = sessaoCaixaRepository.findAll();
        assertThat(sessaoCaixaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkValorAtualIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessaoCaixaRepository.findAll().size();
        // set the field null
        sessaoCaixa.setValorAtual(null);

        // Create the SessaoCaixa, which fails.

        restSessaoCaixaMockMvc.perform(post("/api/sessao-caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessaoCaixa)))
            .andExpect(status().isBadRequest());

        List<SessaoCaixa> sessaoCaixaList = sessaoCaixaRepository.findAll();
        assertThat(sessaoCaixaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessaoCaixaRepository.findAll().size();
        // set the field null
        sessaoCaixa.setData(null);

        // Create the SessaoCaixa, which fails.

        restSessaoCaixaMockMvc.perform(post("/api/sessao-caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessaoCaixa)))
            .andExpect(status().isBadRequest());

        List<SessaoCaixa> sessaoCaixaList = sessaoCaixaRepository.findAll();
        assertThat(sessaoCaixaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSessaoCaixas() throws Exception {
        // Initialize the database
        sessaoCaixaRepository.saveAndFlush(sessaoCaixa);

        // Get all the sessaoCaixaList
        restSessaoCaixaMockMvc.perform(get("/api/sessao-caixas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessaoCaixa.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorAtual").value(hasItem(DEFAULT_VALOR_ATUAL.doubleValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }
    
    @Test
    @Transactional
    public void getSessaoCaixa() throws Exception {
        // Initialize the database
        sessaoCaixaRepository.saveAndFlush(sessaoCaixa);

        // Get the sessaoCaixa
        restSessaoCaixaMockMvc.perform(get("/api/sessao-caixas/{id}", sessaoCaixa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sessaoCaixa.getId().intValue()))
            .andExpect(jsonPath("$.valorAtual").value(DEFAULT_VALOR_ATUAL.doubleValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSessaoCaixa() throws Exception {
        // Get the sessaoCaixa
        restSessaoCaixaMockMvc.perform(get("/api/sessao-caixas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSessaoCaixa() throws Exception {
        // Initialize the database
        sessaoCaixaRepository.saveAndFlush(sessaoCaixa);

        int databaseSizeBeforeUpdate = sessaoCaixaRepository.findAll().size();

        // Update the sessaoCaixa
        SessaoCaixa updatedSessaoCaixa = sessaoCaixaRepository.findById(sessaoCaixa.getId()).get();
        // Disconnect from session so that the updates on updatedSessaoCaixa are not directly saved in db
        em.detach(updatedSessaoCaixa);
        updatedSessaoCaixa
            .valorAtual(UPDATED_VALOR_ATUAL)
            .data(UPDATED_DATA);

        restSessaoCaixaMockMvc.perform(put("/api/sessao-caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSessaoCaixa)))
            .andExpect(status().isOk());

        // Validate the SessaoCaixa in the database
        List<SessaoCaixa> sessaoCaixaList = sessaoCaixaRepository.findAll();
        assertThat(sessaoCaixaList).hasSize(databaseSizeBeforeUpdate);
        SessaoCaixa testSessaoCaixa = sessaoCaixaList.get(sessaoCaixaList.size() - 1);
        assertThat(testSessaoCaixa.getValorAtual()).isEqualTo(UPDATED_VALOR_ATUAL);
        assertThat(testSessaoCaixa.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    public void updateNonExistingSessaoCaixa() throws Exception {
        int databaseSizeBeforeUpdate = sessaoCaixaRepository.findAll().size();

        // Create the SessaoCaixa

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessaoCaixaMockMvc.perform(put("/api/sessao-caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sessaoCaixa)))
            .andExpect(status().isBadRequest());

        // Validate the SessaoCaixa in the database
        List<SessaoCaixa> sessaoCaixaList = sessaoCaixaRepository.findAll();
        assertThat(sessaoCaixaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSessaoCaixa() throws Exception {
        // Initialize the database
        sessaoCaixaRepository.saveAndFlush(sessaoCaixa);

        int databaseSizeBeforeDelete = sessaoCaixaRepository.findAll().size();

        // Delete the sessaoCaixa
        restSessaoCaixaMockMvc.perform(delete("/api/sessao-caixas/{id}", sessaoCaixa.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SessaoCaixa> sessaoCaixaList = sessaoCaixaRepository.findAll();
        assertThat(sessaoCaixaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessaoCaixa.class);
        SessaoCaixa sessaoCaixa1 = new SessaoCaixa();
        sessaoCaixa1.setId(1L);
        SessaoCaixa sessaoCaixa2 = new SessaoCaixa();
        sessaoCaixa2.setId(sessaoCaixa1.getId());
        assertThat(sessaoCaixa1).isEqualTo(sessaoCaixa2);
        sessaoCaixa2.setId(2L);
        assertThat(sessaoCaixa1).isNotEqualTo(sessaoCaixa2);
        sessaoCaixa1.setId(null);
        assertThat(sessaoCaixa1).isNotEqualTo(sessaoCaixa2);
    }
}
