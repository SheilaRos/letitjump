package com.bprogramers.letitjump.web.rest;

import com.bprogramers.letitjump.LetItJumpApp;

import com.bprogramers.letitjump.domain.PowerUp;
import com.bprogramers.letitjump.repository.PowerUpRepository;

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
 * Test class for the PowerUpResource REST controller.
 *
 * @see PowerUpResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetItJumpApp.class)
public class PowerUpResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PRICE_GAME = 1L;
    private static final Long UPDATED_PRICE_GAME = 2L;

    private static final Long DEFAULT_PRICE_PREMIUM = 1L;
    private static final Long UPDATED_PRICE_PREMIUM = 2L;

    private static final String DEFAULT_SPLASH_ART = "AAAAAAAAAA";
    private static final String UPDATED_SPLASH_ART = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIME = 1;
    private static final Integer UPDATED_TIME = 2;

    private static final String DEFAULT_ATTR = "AAAAAAAAAA";
    private static final String UPDATED_ATTR = "BBBBBBBBBB";

    private static final Integer DEFAULT_ATTR_VALUE = 1;
    private static final Integer UPDATED_ATTR_VALUE = 2;

    @Inject
    private PowerUpRepository powerUpRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPowerUpMockMvc;

    private PowerUp powerUp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PowerUpResource powerUpResource = new PowerUpResource();
        ReflectionTestUtils.setField(powerUpResource, "powerUpRepository", powerUpRepository);
        this.restPowerUpMockMvc = MockMvcBuilders.standaloneSetup(powerUpResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PowerUp createEntity(EntityManager em) {
        PowerUp powerUp = new PowerUp()
                .name(DEFAULT_NAME)
                .priceGame(DEFAULT_PRICE_GAME)
                .pricePremium(DEFAULT_PRICE_PREMIUM)
                .splashArt(DEFAULT_SPLASH_ART)
                .time(DEFAULT_TIME)
                .attr(DEFAULT_ATTR)
                .attrValue(DEFAULT_ATTR_VALUE);
        return powerUp;
    }

    @Before
    public void initTest() {
        powerUp = createEntity(em);
    }

    @Test
    @Transactional
    public void createPowerUp() throws Exception {
        int databaseSizeBeforeCreate = powerUpRepository.findAll().size();

        // Create the PowerUp

        restPowerUpMockMvc.perform(post("/api/power-ups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(powerUp)))
            .andExpect(status().isCreated());

        // Validate the PowerUp in the database
        List<PowerUp> powerUps = powerUpRepository.findAll();
        assertThat(powerUps).hasSize(databaseSizeBeforeCreate + 1);
        PowerUp testPowerUp = powerUps.get(powerUps.size() - 1);
        assertThat(testPowerUp.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPowerUp.getPriceGame()).isEqualTo(DEFAULT_PRICE_GAME);
        assertThat(testPowerUp.getPricePremium()).isEqualTo(DEFAULT_PRICE_PREMIUM);
        assertThat(testPowerUp.getSplashArt()).isEqualTo(DEFAULT_SPLASH_ART);
        assertThat(testPowerUp.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testPowerUp.getAttr()).isEqualTo(DEFAULT_ATTR);
        assertThat(testPowerUp.getAttrValue()).isEqualTo(DEFAULT_ATTR_VALUE);
    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = powerUpRepository.findAll().size();
        // set the field null
        powerUp.setTime(null);

        // Create the PowerUp, which fails.

        restPowerUpMockMvc.perform(post("/api/power-ups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(powerUp)))
            .andExpect(status().isBadRequest());

        List<PowerUp> powerUps = powerUpRepository.findAll();
        assertThat(powerUps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAttrIsRequired() throws Exception {
        int databaseSizeBeforeTest = powerUpRepository.findAll().size();
        // set the field null
        powerUp.setAttr(null);

        // Create the PowerUp, which fails.

        restPowerUpMockMvc.perform(post("/api/power-ups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(powerUp)))
            .andExpect(status().isBadRequest());

        List<PowerUp> powerUps = powerUpRepository.findAll();
        assertThat(powerUps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAttrValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = powerUpRepository.findAll().size();
        // set the field null
        powerUp.setAttrValue(null);

        // Create the PowerUp, which fails.

        restPowerUpMockMvc.perform(post("/api/power-ups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(powerUp)))
            .andExpect(status().isBadRequest());

        List<PowerUp> powerUps = powerUpRepository.findAll();
        assertThat(powerUps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPowerUps() throws Exception {
        // Initialize the database
        powerUpRepository.saveAndFlush(powerUp);

        // Get all the powerUps
        restPowerUpMockMvc.perform(get("/api/power-ups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(powerUp.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].priceGame").value(hasItem(DEFAULT_PRICE_GAME.intValue())))
            .andExpect(jsonPath("$.[*].pricePremium").value(hasItem(DEFAULT_PRICE_PREMIUM.intValue())))
            .andExpect(jsonPath("$.[*].splashArt").value(hasItem(DEFAULT_SPLASH_ART.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)))
            .andExpect(jsonPath("$.[*].attr").value(hasItem(DEFAULT_ATTR.toString())))
            .andExpect(jsonPath("$.[*].attrValue").value(hasItem(DEFAULT_ATTR_VALUE)));
    }

    @Test
    @Transactional
    public void getPowerUp() throws Exception {
        // Initialize the database
        powerUpRepository.saveAndFlush(powerUp);

        // Get the powerUp
        restPowerUpMockMvc.perform(get("/api/power-ups/{id}", powerUp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(powerUp.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.priceGame").value(DEFAULT_PRICE_GAME.intValue()))
            .andExpect(jsonPath("$.pricePremium").value(DEFAULT_PRICE_PREMIUM.intValue()))
            .andExpect(jsonPath("$.splashArt").value(DEFAULT_SPLASH_ART.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME))
            .andExpect(jsonPath("$.attr").value(DEFAULT_ATTR.toString()))
            .andExpect(jsonPath("$.attrValue").value(DEFAULT_ATTR_VALUE));
    }

    @Test
    @Transactional
    public void getNonExistingPowerUp() throws Exception {
        // Get the powerUp
        restPowerUpMockMvc.perform(get("/api/power-ups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePowerUp() throws Exception {
        // Initialize the database
        powerUpRepository.saveAndFlush(powerUp);
        int databaseSizeBeforeUpdate = powerUpRepository.findAll().size();

        // Update the powerUp
        PowerUp updatedPowerUp = powerUpRepository.findOne(powerUp.getId());
        updatedPowerUp
                .name(UPDATED_NAME)
                .priceGame(UPDATED_PRICE_GAME)
                .pricePremium(UPDATED_PRICE_PREMIUM)
                .splashArt(UPDATED_SPLASH_ART)
                .time(UPDATED_TIME)
                .attr(UPDATED_ATTR)
                .attrValue(UPDATED_ATTR_VALUE);

        restPowerUpMockMvc.perform(put("/api/power-ups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPowerUp)))
            .andExpect(status().isOk());

        // Validate the PowerUp in the database
        List<PowerUp> powerUps = powerUpRepository.findAll();
        assertThat(powerUps).hasSize(databaseSizeBeforeUpdate);
        PowerUp testPowerUp = powerUps.get(powerUps.size() - 1);
        assertThat(testPowerUp.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPowerUp.getPriceGame()).isEqualTo(UPDATED_PRICE_GAME);
        assertThat(testPowerUp.getPricePremium()).isEqualTo(UPDATED_PRICE_PREMIUM);
        assertThat(testPowerUp.getSplashArt()).isEqualTo(UPDATED_SPLASH_ART);
        assertThat(testPowerUp.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testPowerUp.getAttr()).isEqualTo(UPDATED_ATTR);
        assertThat(testPowerUp.getAttrValue()).isEqualTo(UPDATED_ATTR_VALUE);
    }

    @Test
    @Transactional
    public void deletePowerUp() throws Exception {
        // Initialize the database
        powerUpRepository.saveAndFlush(powerUp);
        int databaseSizeBeforeDelete = powerUpRepository.findAll().size();

        // Get the powerUp
        restPowerUpMockMvc.perform(delete("/api/power-ups/{id}", powerUp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PowerUp> powerUps = powerUpRepository.findAll();
        assertThat(powerUps).hasSize(databaseSizeBeforeDelete - 1);
    }
}
