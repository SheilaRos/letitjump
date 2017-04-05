package com.bprogramers.letitjump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bprogramers.letitjump.domain.Map;

import com.bprogramers.letitjump.repository.MapRepository;
import com.bprogramers.letitjump.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Map.
 */
@RestController
@RequestMapping("/api")
public class MapResource {

    private final Logger log = LoggerFactory.getLogger(MapResource.class);
        
    @Inject
    private MapRepository mapRepository;

    /**
     * POST  /maps : Create a new map.
     *
     * @param map the map to create
     * @return the ResponseEntity with status 201 (Created) and with body the new map, or with status 400 (Bad Request) if the map has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/maps")
    @Timed
    public ResponseEntity<Map> createMap(@RequestBody Map map) throws URISyntaxException {
        log.debug("REST request to save Map : {}", map);
        if (map.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("map", "idexists", "A new map cannot already have an ID")).body(null);
        }
        Map result = mapRepository.save(map);
        return ResponseEntity.created(new URI("/api/maps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("map", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /maps : Updates an existing map.
     *
     * @param map the map to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated map,
     * or with status 400 (Bad Request) if the map is not valid,
     * or with status 500 (Internal Server Error) if the map couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/maps")
    @Timed
    public ResponseEntity<Map> updateMap(@RequestBody Map map) throws URISyntaxException {
        log.debug("REST request to update Map : {}", map);
        if (map.getId() == null) {
            return createMap(map);
        }
        Map result = mapRepository.save(map);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("map", map.getId().toString()))
            .body(result);
    }

    /**
     * GET  /maps : get all the maps.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of maps in body
     */
    @GetMapping("/maps")
    @Timed
    public List<Map> getAllMaps(@RequestParam(required = false) String filter) {
        if ("level-is-null".equals(filter)) {
            log.debug("REST request to get all Maps where level is null");
            return StreamSupport
                .stream(mapRepository.findAll().spliterator(), false)
                .filter(map -> map.getLevel() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Maps");
        List<Map> maps = mapRepository.findAll();
        return maps;
    }

    /**
     * GET  /maps/:id : get the "id" map.
     *
     * @param id the id of the map to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the map, or with status 404 (Not Found)
     */
    @GetMapping("/maps/{id}")
    @Timed
    public ResponseEntity<Map> getMap(@PathVariable Long id) {
        log.debug("REST request to get Map : {}", id);
        Map map = mapRepository.findOne(id);
        return Optional.ofNullable(map)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /maps/:id : delete the "id" map.
     *
     * @param id the id of the map to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/maps/{id}")
    @Timed
    public ResponseEntity<Void> deleteMap(@PathVariable Long id) {
        log.debug("REST request to delete Map : {}", id);
        mapRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("map", id.toString())).build();
    }

}
