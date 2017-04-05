package com.bprogramers.letitjump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bprogramers.letitjump.domain.ForumEntry;

import com.bprogramers.letitjump.repository.ForumEntryRepository;
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

/**
 * REST controller for managing ForumEntry.
 */
@RestController
@RequestMapping("/api")
public class ForumEntryResource {

    private final Logger log = LoggerFactory.getLogger(ForumEntryResource.class);
        
    @Inject
    private ForumEntryRepository forumEntryRepository;

    /**
     * POST  /forum-entries : Create a new forumEntry.
     *
     * @param forumEntry the forumEntry to create
     * @return the ResponseEntity with status 201 (Created) and with body the new forumEntry, or with status 400 (Bad Request) if the forumEntry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/forum-entries")
    @Timed
    public ResponseEntity<ForumEntry> createForumEntry(@RequestBody ForumEntry forumEntry) throws URISyntaxException {
        log.debug("REST request to save ForumEntry : {}", forumEntry);
        if (forumEntry.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("forumEntry", "idexists", "A new forumEntry cannot already have an ID")).body(null);
        }
        ForumEntry result = forumEntryRepository.save(forumEntry);
        return ResponseEntity.created(new URI("/api/forum-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("forumEntry", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /forum-entries : Updates an existing forumEntry.
     *
     * @param forumEntry the forumEntry to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated forumEntry,
     * or with status 400 (Bad Request) if the forumEntry is not valid,
     * or with status 500 (Internal Server Error) if the forumEntry couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/forum-entries")
    @Timed
    public ResponseEntity<ForumEntry> updateForumEntry(@RequestBody ForumEntry forumEntry) throws URISyntaxException {
        log.debug("REST request to update ForumEntry : {}", forumEntry);
        if (forumEntry.getId() == null) {
            return createForumEntry(forumEntry);
        }
        ForumEntry result = forumEntryRepository.save(forumEntry);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("forumEntry", forumEntry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /forum-entries : get all the forumEntries.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of forumEntries in body
     */
    @GetMapping("/forum-entries")
    @Timed
    public List<ForumEntry> getAllForumEntries() {
        log.debug("REST request to get all ForumEntries");
        List<ForumEntry> forumEntries = forumEntryRepository.findAll();
        return forumEntries;
    }

    /**
     * GET  /forum-entries/:id : get the "id" forumEntry.
     *
     * @param id the id of the forumEntry to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the forumEntry, or with status 404 (Not Found)
     */
    @GetMapping("/forum-entries/{id}")
    @Timed
    public ResponseEntity<ForumEntry> getForumEntry(@PathVariable Long id) {
        log.debug("REST request to get ForumEntry : {}", id);
        ForumEntry forumEntry = forumEntryRepository.findOne(id);
        return Optional.ofNullable(forumEntry)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /forum-entries/:id : delete the "id" forumEntry.
     *
     * @param id the id of the forumEntry to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/forum-entries/{id}")
    @Timed
    public ResponseEntity<Void> deleteForumEntry(@PathVariable Long id) {
        log.debug("REST request to delete ForumEntry : {}", id);
        forumEntryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("forumEntry", id.toString())).build();
    }

}
