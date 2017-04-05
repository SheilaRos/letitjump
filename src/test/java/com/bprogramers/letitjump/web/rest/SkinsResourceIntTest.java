package com.bprogramers.letitjump.web.rest;

import com.bprogramers.letitjump.LetItJumpApp;

import com.bprogramers.letitjump.domain.Skins;
import com.bprogramers.letitjump.repository.SkinsRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SkinsResource REST controller.
 *
 * @see SkinsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetItJumpApp.class)
public class SkinsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PRICE_GAME = 1L;
    private static final Long UPDATED_PRICE_GAME = 2L;

    private static final Long DEFAULT_PRICE_PREMIUM = 1L;
    private static final Long UPDATED_PRICE_PREMIUM = 2L;

    private static final String DEFAULT_SPLASH_ART = "AAAAAAAAAA";
    private static final String UPDATED_SPLASH_ART = "BBBBBBBBBB";

    private static final String DEFAULT_ATTR = "AAAAAAAAAA";
    private static final String UPDATED_ATTR = "BBBBBBBBBB";

    private static final Integer DEFAULT_ATTR_VALUE = 1;
    private static final Integer UPDATED_ATTR_VALUE = 2;

    @Inject
    private SkinsRepository skinsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSkinsMockMvc;

    private Skins skins;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SkinsResource skinsResource = new SkinsResource();
        ReflectionTestUtils.setField(skinsResource, "skinsRepository", skinsRepository);
        this.restSkinsMockMvc = MockMvcBuilders.standaloneSetup(skinsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skins createEntity(EntityManager em) {
        Skins skins = new Skins()
                .name(DEFAULT_NAME)
                .priceGame(DEFAULT_PRICE_GAME)
                .pricePremium(DEFAULT_PRICE_PREMIUM)
                .splashArt(DEFAULT_SPLASH_ART)
                .attr(DEFAULT_ATTR)
                .attrValue(DEFAULT_ATTR_VALUE);
        return skins;
    }

    @Before
    public void initTest() {
        skins = createEntity(em);
    }

    @Test
    @Transactional
    public void createSkins() throws Exception {
        int databaseSizeBeforeCreate = skinsRepository.findAll().size();

        // Create the Skins

        restSkinsMockMvc.perform(post("/api/skins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skins)))
            .andExpect(status().isCreated());

        // Validate the Skins in the database
        List<Skins> skinsList = skinsRepository.findAll();
        assertThat(skinsList).hasSize(databaseSizeBeforeCreate + 1);
        Skins testSkins = skinsList.get(skinsList.size() - 1);
        assertThat(testSkins.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSkins.getPriceGame()).isEqualTo(DEFAULT_PRICE_GAME);
        assertThat(testSkins.getPricePremium()).isEqualTo(DEFAULT_PRICE_PREMIUM);
        assertThat(testSkins.getSplashArt()).isEqualTo(DEFAULT_SPLASH_ART);
        assertThat(testSkins.getAttr()).isEqualTo(DEFAULT_ATTR);
        assertThat(testSkins.getAttrValue()).isEqualTo(DEFAULT_ATTR_VALUE);
    }

    @Test
    @Transactional
    public void createSkinsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = skinsRepository.findAll().size();

        // Create the Skins with an existing ID
        Skins existingSkins = new Skins();
        existingSkins.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkinsMockMvc.perform(post("/api/skins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSkins)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Skins> skinsList = skinsRepository.findAll();
        assertThat(skinsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAttrIsRequired() throws Exception {
        int databaseSizeBeforeTest = skinsRepository.findAll().size();
        // set the field null
        skins.setAttr(null);

        // Create the Skins, which fails.

        restSkinsMockMvc.perform(post("/api/skins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skins)))
            .andExpect(status().isBadRequest());

        List<Skins> skinsList = skinsRepository.findAll();
        assertThat(skinsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAttrValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = skinsRepository.findAll().size();
        // set the field null
        skins.setAttrValue(null);

        // Create the Skins, which fails.

        restSkinsMockMvc.perform(post("/api/skins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skins)))
            .andExpect(status().isBadRequest());

        List<Skins> skinsList = skinsRepository.findAll();
        assertThat(skinsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSkins() throws Exception {
        // Initialize the database
        skinsRepository.saveAndFlush(skins);

        // Get all the skinsList
        restSkinsMockMvc.perform(get("/api/skins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skins.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].priceGame").value(hasItem(DEFAULT_PRICE_GAME.intValue())))
            .andExpect(jsonPath("$.[*].pricePremium").value(hasItem(DEFAULT_PRICE_PREMIUM.intValue())))
            .andExpect(jsonPath("$.[*].splashArt").value(hasItem(DEFAULT_SPLASH_ART.toString())))
            .andExpect(jsonPath("$.[*].attr").value(hasItem(DEFAULT_ATTR.toString())))
            .andExpect(jsonPath("$.[*].attrValue").value(hasItem(DEFAULT_ATTR_VALUE)));
    }

    @Test
    @Transactional
    public void getSkins() throws Exception {
        // Initialize the database
        skinsRepository.saveAndFlush(skins);

        // Get the skins
        restSkinsMockMvc.perform(get("/api/skins/{id}", skins.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(skins.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.priceGame").value(DEFAULT_PRICE_GAME.intValue()))
            .andExpect(jsonPath("$.pricePremium").value(DEFAULT_PRICE_PREMIUM.intValue()))
            .andExpect(jsonPath("$.splashArt").value(DEFAULT_SPLASH_ART.toString()))
            .andExpect(jsonPath("$.attr").value(DEFAULT_ATTR.toString()))
            .andExpect(jsonPath("$.attrValue").value(DEFAULT_ATTR_VALUE));
    }

    @Test
    @Transactional
    public void getNonExistingSkins() throws Exception {
        // Get the skins
        restSkinsMockMvc.perform(get("/api/skins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkins() throws Exception {
        // Initialize the database
        skinsRepository.saveAndFlush(skins);
        int databaseSizeBeforeUpdate = skinsRepository.findAll().size();

        // Update the skins
        Skins updatedSkins = skinsRepository.findOne(skins.getId());
        updatedSkins
                .name(UPDATED_NAME)
                .priceGame(UPDATED_PRICE_GAME)
                .pricePremium(UPDATED_PRICE_PREMIUM)
                .splashArt(UPDATED_SPLASH_ART)
                .attr(UPDATED_ATTR)
                .attrValue(UPDATED_ATTR_VALUE);

        restSkinsMockMvc.perform(put("/api/skins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSkins)))
            .andExpect(status().isOk());

        // Validate the Skins in the database
        List<Skins> skinsList = skinsRepository.findAll();
        assertThat(skinsList).hasSize(databaseSizeBeforeUpdate);
        Skins testSkins = skinsList.get(skinsList.size() - 1);
        assertThat(testSkins.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSkins.getPriceGame()).isEqualTo(UPDATED_PRICE_GAME);
        assertThat(testSkins.getPricePremium()).isEqualTo(UPDATED_PRICE_PREMIUM);
        assertThat(testSkins.getSplashArt()).isEqualTo(UPDATED_SPLASH_ART);
        assertThat(testSkins.getAttr()).isEqualTo(UPDATED_ATTR);
        assertThat(testSkins.getAttrValue()).isEqualTo(UPDATED_ATTR_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingSkins() throws Exception {
        int databaseSizeBeforeUpdate = skinsRepository.findAll().size();

        // Create the Skins

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSkinsMockMvc.perform(put("/api/skins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skins)))
            .andExpect(status().isCreated());

        // Validate the Skins in the database
        List<Skins> skinsList = skinsRepository.findAll();
        assertThat(skinsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSkins() throws Exception {
        // Initialize the database
        skinsRepository.saveAndFlush(skins);
        int databaseSizeBeforeDelete = skinsRepository.findAll().size();

        // Get the skins
        restSkinsMockMvc.perform(delete("/api/skins/{id}", skins.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Skins> skinsList = skinsRepository.findAll();
        assertThat(skinsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
