package com.bprogramers.letitjump.web.rest;

import com.bprogramers.letitjump.LetItJumpApp;

import com.bprogramers.letitjump.domain.UserCustomAtributes;
import com.bprogramers.letitjump.repository.UserCustomAtributesRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserCustomAtributesResource REST controller.
 *
 * @see UserCustomAtributesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetItJumpApp.class)
public class UserCustomAtributesResourceIntTest {

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_MONEY_GAME = 1L;
    private static final Long UPDATED_MONEY_GAME = 2L;

    private static final Long DEFAULT_MONEY_PREMIUM = 1L;
    private static final Long UPDATED_MONEY_PREMIUM = 2L;

    private static final Long DEFAULT_SCORE = 1L;
    private static final Long UPDATED_SCORE = 2L;

    private static final String DEFAULT_SEX = "AAAAAAAAAA";
    private static final String UPDATED_SEX = "BBBBBBBBBB";

    @Inject
    private UserCustomAtributesRepository userCustomAtributesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserCustomAtributesMockMvc;

    private UserCustomAtributes userCustomAtributes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserCustomAtributesResource userCustomAtributesResource = new UserCustomAtributesResource();
        ReflectionTestUtils.setField(userCustomAtributesResource, "userCustomAtributesRepository", userCustomAtributesRepository);
        this.restUserCustomAtributesMockMvc = MockMvcBuilders.standaloneSetup(userCustomAtributesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserCustomAtributes createEntity(EntityManager em) {
        UserCustomAtributes userCustomAtributes = new UserCustomAtributes()
                .birthday(DEFAULT_BIRTHDAY)
                .moneyGame(DEFAULT_MONEY_GAME)
                .moneyPremium(DEFAULT_MONEY_PREMIUM)
                .score(DEFAULT_SCORE)
                .sex(DEFAULT_SEX);
        return userCustomAtributes;
    }

    @Before
    public void initTest() {
        userCustomAtributes = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserCustomAtributes() throws Exception {
        int databaseSizeBeforeCreate = userCustomAtributesRepository.findAll().size();

        // Create the UserCustomAtributes

        restUserCustomAtributesMockMvc.perform(post("/api/user-custom-atributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCustomAtributes)))
            .andExpect(status().isCreated());

        // Validate the UserCustomAtributes in the database
        List<UserCustomAtributes> userCustomAtributesList = userCustomAtributesRepository.findAll();
        assertThat(userCustomAtributesList).hasSize(databaseSizeBeforeCreate + 1);
        UserCustomAtributes testUserCustomAtributes = userCustomAtributesList.get(userCustomAtributesList.size() - 1);
        assertThat(testUserCustomAtributes.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testUserCustomAtributes.getMoneyGame()).isEqualTo(DEFAULT_MONEY_GAME);
        assertThat(testUserCustomAtributes.getMoneyPremium()).isEqualTo(DEFAULT_MONEY_PREMIUM);
        assertThat(testUserCustomAtributes.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testUserCustomAtributes.getSex()).isEqualTo(DEFAULT_SEX);
    }

    @Test
    @Transactional
    public void createUserCustomAtributesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userCustomAtributesRepository.findAll().size();

        // Create the UserCustomAtributes with an existing ID
        UserCustomAtributes existingUserCustomAtributes = new UserCustomAtributes();
        existingUserCustomAtributes.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserCustomAtributesMockMvc.perform(post("/api/user-custom-atributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingUserCustomAtributes)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserCustomAtributes> userCustomAtributesList = userCustomAtributesRepository.findAll();
        assertThat(userCustomAtributesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserCustomAtributes() throws Exception {
        // Initialize the database
        userCustomAtributesRepository.saveAndFlush(userCustomAtributes);

        // Get all the userCustomAtributesList
        restUserCustomAtributesMockMvc.perform(get("/api/user-custom-atributes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userCustomAtributes.getId().intValue())))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].moneyGame").value(hasItem(DEFAULT_MONEY_GAME.intValue())))
            .andExpect(jsonPath("$.[*].moneyPremium").value(hasItem(DEFAULT_MONEY_PREMIUM.intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.intValue())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())));
    }

    @Test
    @Transactional
    public void getUserCustomAtributes() throws Exception {
        // Initialize the database
        userCustomAtributesRepository.saveAndFlush(userCustomAtributes);

        // Get the userCustomAtributes
        restUserCustomAtributesMockMvc.perform(get("/api/user-custom-atributes/{id}", userCustomAtributes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userCustomAtributes.getId().intValue()))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.moneyGame").value(DEFAULT_MONEY_GAME.intValue()))
            .andExpect(jsonPath("$.moneyPremium").value(DEFAULT_MONEY_PREMIUM.intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.intValue()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserCustomAtributes() throws Exception {
        // Get the userCustomAtributes
        restUserCustomAtributesMockMvc.perform(get("/api/user-custom-atributes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserCustomAtributes() throws Exception {
        // Initialize the database
        userCustomAtributesRepository.saveAndFlush(userCustomAtributes);
        int databaseSizeBeforeUpdate = userCustomAtributesRepository.findAll().size();

        // Update the userCustomAtributes
        UserCustomAtributes updatedUserCustomAtributes = userCustomAtributesRepository.findOne(userCustomAtributes.getId());
        updatedUserCustomAtributes
                .birthday(UPDATED_BIRTHDAY)
                .moneyGame(UPDATED_MONEY_GAME)
                .moneyPremium(UPDATED_MONEY_PREMIUM)
                .score(UPDATED_SCORE)
                .sex(UPDATED_SEX);

        restUserCustomAtributesMockMvc.perform(put("/api/user-custom-atributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserCustomAtributes)))
            .andExpect(status().isOk());

        // Validate the UserCustomAtributes in the database
        List<UserCustomAtributes> userCustomAtributesList = userCustomAtributesRepository.findAll();
        assertThat(userCustomAtributesList).hasSize(databaseSizeBeforeUpdate);
        UserCustomAtributes testUserCustomAtributes = userCustomAtributesList.get(userCustomAtributesList.size() - 1);
        assertThat(testUserCustomAtributes.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testUserCustomAtributes.getMoneyGame()).isEqualTo(UPDATED_MONEY_GAME);
        assertThat(testUserCustomAtributes.getMoneyPremium()).isEqualTo(UPDATED_MONEY_PREMIUM);
        assertThat(testUserCustomAtributes.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testUserCustomAtributes.getSex()).isEqualTo(UPDATED_SEX);
    }

    @Test
    @Transactional
    public void updateNonExistingUserCustomAtributes() throws Exception {
        int databaseSizeBeforeUpdate = userCustomAtributesRepository.findAll().size();

        // Create the UserCustomAtributes

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserCustomAtributesMockMvc.perform(put("/api/user-custom-atributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userCustomAtributes)))
            .andExpect(status().isCreated());

        // Validate the UserCustomAtributes in the database
        List<UserCustomAtributes> userCustomAtributesList = userCustomAtributesRepository.findAll();
        assertThat(userCustomAtributesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserCustomAtributes() throws Exception {
        // Initialize the database
        userCustomAtributesRepository.saveAndFlush(userCustomAtributes);
        int databaseSizeBeforeDelete = userCustomAtributesRepository.findAll().size();

        // Get the userCustomAtributes
        restUserCustomAtributesMockMvc.perform(delete("/api/user-custom-atributes/{id}", userCustomAtributes.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserCustomAtributes> userCustomAtributesList = userCustomAtributesRepository.findAll();
        assertThat(userCustomAtributesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
