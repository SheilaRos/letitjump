package com.bprogramers.letitjump.web.rest;

import com.bprogramers.letitjump.LetItJumpApp;

import com.bprogramers.letitjump.domain.Map;
import com.bprogramers.letitjump.repository.MapRepository;

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
 * Test class for the MapResource REST controller.
 *
 * @see MapResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LetItJumpApp.class)
public class MapResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private MapRepository mapRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMapMockMvc;

    private Map map;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MapResource mapResource = new MapResource();
        ReflectionTestUtils.setField(mapResource, "mapRepository", mapRepository);
        this.restMapMockMvc = MockMvcBuilders.standaloneSetup(mapResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Map createEntity(EntityManager em) {
        Map map = new Map()
                .name(DEFAULT_NAME);
        return map;
    }

    @Before
    public void initTest() {
        map = createEntity(em);
    }

    @Test
    @Transactional
    public void createMap() throws Exception {
        int databaseSizeBeforeCreate = mapRepository.findAll().size();

        // Create the Map

        restMapMockMvc.perform(post("/api/maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(map)))
            .andExpect(status().isCreated());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeCreate + 1);
        Map testMap = mapList.get(mapList.size() - 1);
        assertThat(testMap.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createMapWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mapRepository.findAll().size();

        // Create the Map with an existing ID
        Map existingMap = new Map();
        existingMap.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMapMockMvc.perform(post("/api/maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingMap)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMaps() throws Exception {
        // Initialize the database
        mapRepository.saveAndFlush(map);

        // Get all the mapList
        restMapMockMvc.perform(get("/api/maps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(map.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getMap() throws Exception {
        // Initialize the database
        mapRepository.saveAndFlush(map);

        // Get the map
        restMapMockMvc.perform(get("/api/maps/{id}", map.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(map.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMap() throws Exception {
        // Get the map
        restMapMockMvc.perform(get("/api/maps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMap() throws Exception {
        // Initialize the database
        mapRepository.saveAndFlush(map);
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();

        // Update the map
        Map updatedMap = mapRepository.findOne(map.getId());
        updatedMap
                .name(UPDATED_NAME);

        restMapMockMvc.perform(put("/api/maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMap)))
            .andExpect(status().isOk());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
        Map testMap = mapList.get(mapList.size() - 1);
        assertThat(testMap.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();

        // Create the Map

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMapMockMvc.perform(put("/api/maps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(map)))
            .andExpect(status().isCreated());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMap() throws Exception {
        // Initialize the database
        mapRepository.saveAndFlush(map);
        int databaseSizeBeforeDelete = mapRepository.findAll().size();

        // Get the map
        restMapMockMvc.perform(delete("/api/maps/{id}", map.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
