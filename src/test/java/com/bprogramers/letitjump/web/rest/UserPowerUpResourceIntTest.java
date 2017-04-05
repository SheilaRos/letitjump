package com.bprogramers.letitjump.web.rest;

import com.bprogramers.letitjump.LetItJumpApp;

import com.bprogramers.letitjump.domain.UserPowerUp;
import com.bprogramers.letitjump.domain.User;
import com.bprogramers.letitjump.domain.PowerUp;
import com.bprogramers.letitjump.repository.UserPowerUpRepository;

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
 * Test class for the UserPowerUpResource REST controller.
 *
 * @see UserPowerUpResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetItJumpApp.class)
public class UserPowerUpResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    @Inject
    private UserPowerUpRepository userPowerUpRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserPowerUpMockMvc;

    private UserPowerUp userPowerUp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserPowerUpResource userPowerUpResource = new UserPowerUpResource();
        ReflectionTestUtils.setField(userPowerUpResource, "userPowerUpRepository", userPowerUpRepository);
        this.restUserPowerUpMockMvc = MockMvcBuilders.standaloneSetup(userPowerUpResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPowerUp createEntity(EntityManager em) {
        UserPowerUp userPowerUp = new UserPowerUp()
                .quantity(DEFAULT_QUANTITY);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        userPowerUp.setUser(user);
        // Add required entity
        PowerUp powerUp = PowerUpResourceIntTest.createEntity(em);
        em.persist(powerUp);
        em.flush();
        userPowerUp.setPowerUp(powerUp);
        return userPowerUp;
    }

    @Before
    public void initTest() {
        userPowerUp = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserPowerUp() throws Exception {
        int databaseSizeBeforeCreate = userPowerUpRepository.findAll().size();

        // Create the UserPowerUp

        restUserPowerUpMockMvc.perform(post("/api/user-power-ups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPowerUp)))
            .andExpect(status().isCreated());

        // Validate the UserPowerUp in the database
        List<UserPowerUp> userPowerUpList = userPowerUpRepository.findAll();
        assertThat(userPowerUpList).hasSize(databaseSizeBeforeCreate + 1);
        UserPowerUp testUserPowerUp = userPowerUpList.get(userPowerUpList.size() - 1);
        assertThat(testUserPowerUp.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createUserPowerUpWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userPowerUpRepository.findAll().size();

        // Create the UserPowerUp with an existing ID
        UserPowerUp existingUserPowerUp = new UserPowerUp();
        existingUserPowerUp.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPowerUpMockMvc.perform(post("/api/user-power-ups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingUserPowerUp)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserPowerUp> userPowerUpList = userPowerUpRepository.findAll();
        assertThat(userPowerUpList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPowerUpRepository.findAll().size();
        // set the field null
        userPowerUp.setQuantity(null);

        // Create the UserPowerUp, which fails.

        restUserPowerUpMockMvc.perform(post("/api/user-power-ups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPowerUp)))
            .andExpect(status().isBadRequest());

        List<UserPowerUp> userPowerUpList = userPowerUpRepository.findAll();
        assertThat(userPowerUpList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserPowerUps() throws Exception {
        // Initialize the database
        userPowerUpRepository.saveAndFlush(userPowerUp);

        // Get all the userPowerUpList
        restUserPowerUpMockMvc.perform(get("/api/user-power-ups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPowerUp.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void getUserPowerUp() throws Exception {
        // Initialize the database
        userPowerUpRepository.saveAndFlush(userPowerUp);

        // Get the userPowerUp
        restUserPowerUpMockMvc.perform(get("/api/user-power-ups/{id}", userPowerUp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userPowerUp.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingUserPowerUp() throws Exception {
        // Get the userPowerUp
        restUserPowerUpMockMvc.perform(get("/api/user-power-ups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserPowerUp() throws Exception {
        // Initialize the database
        userPowerUpRepository.saveAndFlush(userPowerUp);
        int databaseSizeBeforeUpdate = userPowerUpRepository.findAll().size();

        // Update the userPowerUp
        UserPowerUp updatedUserPowerUp = userPowerUpRepository.findOne(userPowerUp.getId());
        updatedUserPowerUp
                .quantity(UPDATED_QUANTITY);

        restUserPowerUpMockMvc.perform(put("/api/user-power-ups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserPowerUp)))
            .andExpect(status().isOk());

        // Validate the UserPowerUp in the database
        List<UserPowerUp> userPowerUpList = userPowerUpRepository.findAll();
        assertThat(userPowerUpList).hasSize(databaseSizeBeforeUpdate);
        UserPowerUp testUserPowerUp = userPowerUpList.get(userPowerUpList.size() - 1);
        assertThat(testUserPowerUp.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingUserPowerUp() throws Exception {
        int databaseSizeBeforeUpdate = userPowerUpRepository.findAll().size();

        // Create the UserPowerUp

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserPowerUpMockMvc.perform(put("/api/user-power-ups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPowerUp)))
            .andExpect(status().isCreated());

        // Validate the UserPowerUp in the database
        List<UserPowerUp> userPowerUpList = userPowerUpRepository.findAll();
        assertThat(userPowerUpList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserPowerUp() throws Exception {
        // Initialize the database
        userPowerUpRepository.saveAndFlush(userPowerUp);
        int databaseSizeBeforeDelete = userPowerUpRepository.findAll().size();

        // Get the userPowerUp
        restUserPowerUpMockMvc.perform(delete("/api/user-power-ups/{id}", userPowerUp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserPowerUp> userPowerUpList = userPowerUpRepository.findAll();
        assertThat(userPowerUpList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
