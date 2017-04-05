package com.bprogramers.letitjump.web.rest;

import com.bprogramers.letitjump.LetItJumpApp;

import com.bprogramers.letitjump.domain.Achievement;
import com.bprogramers.letitjump.repository.AchievementRepository;

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
 * Test class for the AchievementResource REST controller.
 *
 * @see AchievementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetItJumpApp.class)
public class AchievementResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PRIZE = 1L;
    private static final Long UPDATED_PRIZE = 2L;

    @Inject
    private AchievementRepository achievementRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAchievementMockMvc;

    private Achievement achievement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AchievementResource achievementResource = new AchievementResource();
        ReflectionTestUtils.setField(achievementResource, "achievementRepository", achievementRepository);
        this.restAchievementMockMvc = MockMvcBuilders.standaloneSetup(achievementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Achievement createEntity(EntityManager em) {
        Achievement achievement = new Achievement()
                .name(DEFAULT_NAME)
                .prize(DEFAULT_PRIZE);
        return achievement;
    }

    @Before
    public void initTest() {
        achievement = createEntity(em);
    }

    @Test
    @Transactional
    public void createAchievement() throws Exception {
        int databaseSizeBeforeCreate = achievementRepository.findAll().size();

        // Create the Achievement

        restAchievementMockMvc.perform(post("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isCreated());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeCreate + 1);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAchievement.getPrize()).isEqualTo(DEFAULT_PRIZE);
    }

    @Test
    @Transactional
    public void createAchievementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = achievementRepository.findAll().size();

        // Create the Achievement with an existing ID
        Achievement existingAchievement = new Achievement();
        existingAchievement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAchievementMockMvc.perform(post("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAchievement)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAchievements() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        // Get all the achievementList
        restAchievementMockMvc.perform(get("/api/achievements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(achievement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].prize").value(hasItem(DEFAULT_PRIZE.intValue())));
    }

    @Test
    @Transactional
    public void getAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        // Get the achievement
        restAchievementMockMvc.perform(get("/api/achievements/{id}", achievement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(achievement.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.prize").value(DEFAULT_PRIZE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAchievement() throws Exception {
        // Get the achievement
        restAchievementMockMvc.perform(get("/api/achievements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();

        // Update the achievement
        Achievement updatedAchievement = achievementRepository.findOne(achievement.getId());
        updatedAchievement
                .name(UPDATED_NAME)
                .prize(UPDATED_PRIZE);

        restAchievementMockMvc.perform(put("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAchievement)))
            .andExpect(status().isOk());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAchievement.getPrize()).isEqualTo(UPDATED_PRIZE);
    }

    @Test
    @Transactional
    public void updateNonExistingAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();

        // Create the Achievement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAchievementMockMvc.perform(put("/api/achievements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isCreated());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);
        int databaseSizeBeforeDelete = achievementRepository.findAll().size();

        // Get the achievement
        restAchievementMockMvc.perform(delete("/api/achievements/{id}", achievement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
