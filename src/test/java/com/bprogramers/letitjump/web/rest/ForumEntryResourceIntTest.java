package com.bprogramers.letitjump.web.rest;

import com.bprogramers.letitjump.LetItJumpApp;

import com.bprogramers.letitjump.domain.ForumEntry;
import com.bprogramers.letitjump.repository.ForumEntryRepository;

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
 * Test class for the ForumEntryResource REST controller.
 *
 * @see ForumEntryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetItJumpApp.class)
public class ForumEntryResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Inject
    private ForumEntryRepository forumEntryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restForumEntryMockMvc;

    private ForumEntry forumEntry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ForumEntryResource forumEntryResource = new ForumEntryResource();
        ReflectionTestUtils.setField(forumEntryResource, "forumEntryRepository", forumEntryRepository);
        this.restForumEntryMockMvc = MockMvcBuilders.standaloneSetup(forumEntryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ForumEntry createEntity(EntityManager em) {
        ForumEntry forumEntry = new ForumEntry()
                .title(DEFAULT_TITLE)
                .text(DEFAULT_TEXT);
        return forumEntry;
    }

    @Before
    public void initTest() {
        forumEntry = createEntity(em);
    }

    @Test
    @Transactional
    public void createForumEntry() throws Exception {
        int databaseSizeBeforeCreate = forumEntryRepository.findAll().size();

        // Create the ForumEntry

        restForumEntryMockMvc.perform(post("/api/forum-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(forumEntry)))
            .andExpect(status().isCreated());

        // Validate the ForumEntry in the database
        List<ForumEntry> forumEntryList = forumEntryRepository.findAll();
        assertThat(forumEntryList).hasSize(databaseSizeBeforeCreate + 1);
        ForumEntry testForumEntry = forumEntryList.get(forumEntryList.size() - 1);
        assertThat(testForumEntry.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testForumEntry.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void createForumEntryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = forumEntryRepository.findAll().size();

        // Create the ForumEntry with an existing ID
        ForumEntry existingForumEntry = new ForumEntry();
        existingForumEntry.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restForumEntryMockMvc.perform(post("/api/forum-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingForumEntry)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ForumEntry> forumEntryList = forumEntryRepository.findAll();
        assertThat(forumEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllForumEntries() throws Exception {
        // Initialize the database
        forumEntryRepository.saveAndFlush(forumEntry);

        // Get all the forumEntryList
        restForumEntryMockMvc.perform(get("/api/forum-entries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(forumEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getForumEntry() throws Exception {
        // Initialize the database
        forumEntryRepository.saveAndFlush(forumEntry);

        // Get the forumEntry
        restForumEntryMockMvc.perform(get("/api/forum-entries/{id}", forumEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(forumEntry.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingForumEntry() throws Exception {
        // Get the forumEntry
        restForumEntryMockMvc.perform(get("/api/forum-entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateForumEntry() throws Exception {
        // Initialize the database
        forumEntryRepository.saveAndFlush(forumEntry);
        int databaseSizeBeforeUpdate = forumEntryRepository.findAll().size();

        // Update the forumEntry
        ForumEntry updatedForumEntry = forumEntryRepository.findOne(forumEntry.getId());
        updatedForumEntry
                .title(UPDATED_TITLE)
                .text(UPDATED_TEXT);

        restForumEntryMockMvc.perform(put("/api/forum-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedForumEntry)))
            .andExpect(status().isOk());

        // Validate the ForumEntry in the database
        List<ForumEntry> forumEntryList = forumEntryRepository.findAll();
        assertThat(forumEntryList).hasSize(databaseSizeBeforeUpdate);
        ForumEntry testForumEntry = forumEntryList.get(forumEntryList.size() - 1);
        assertThat(testForumEntry.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testForumEntry.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void updateNonExistingForumEntry() throws Exception {
        int databaseSizeBeforeUpdate = forumEntryRepository.findAll().size();

        // Create the ForumEntry

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restForumEntryMockMvc.perform(put("/api/forum-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(forumEntry)))
            .andExpect(status().isCreated());

        // Validate the ForumEntry in the database
        List<ForumEntry> forumEntryList = forumEntryRepository.findAll();
        assertThat(forumEntryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteForumEntry() throws Exception {
        // Initialize the database
        forumEntryRepository.saveAndFlush(forumEntry);
        int databaseSizeBeforeDelete = forumEntryRepository.findAll().size();

        // Get the forumEntry
        restForumEntryMockMvc.perform(delete("/api/forum-entries/{id}", forumEntry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ForumEntry> forumEntryList = forumEntryRepository.findAll();
        assertThat(forumEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
