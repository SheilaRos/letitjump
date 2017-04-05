package com.bprogramers.letitjump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bprogramers.letitjump.domain.Achievement;

import com.bprogramers.letitjump.repository.AchievementRepository;
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
 * REST controller for managing Achievement.
 */
@RestController
@RequestMapping("/api")
public class AchievementResource {

    private final Logger log = LoggerFactory.getLogger(AchievementResource.class);
        
    @Inject
    private AchievementRepository achievementRepository;

    /**
     * POST  /achievements : Create a new achievement.
     *
     * @param achievement the achievement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new achievement, or with status 400 (Bad Request) if the achievement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/achievements")
    @Timed
    public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement) throws URISyntaxException {
        log.debug("REST request to save Achievement : {}", achievement);
        if (achievement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("achievement", "idexists", "A new achievement cannot already have an ID")).body(null);
        }
        Achievement result = achievementRepository.save(achievement);
        return ResponseEntity.created(new URI("/api/achievements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("achievement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /achievements : Updates an existing achievement.
     *
     * @param achievement the achievement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated achievement,
     * or with status 400 (Bad Request) if the achievement is not valid,
     * or with status 500 (Internal Server Error) if the achievement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/achievements")
    @Timed
    public ResponseEntity<Achievement> updateAchievement(@RequestBody Achievement achievement) throws URISyntaxException {
        log.debug("REST request to update Achievement : {}", achievement);
        if (achievement.getId() == null) {
            return createAchievement(achievement);
        }
        Achievement result = achievementRepository.save(achievement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("achievement", achievement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /achievements : get all the achievements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of achievements in body
     */
    @GetMapping("/achievements")
    @Timed
    public List<Achievement> getAllAchievements() {
        log.debug("REST request to get all Achievements");
        List<Achievement> achievements = achievementRepository.findAllWithEagerRelationships();
        return achievements;
    }

    /**
     * GET  /achievements/:id : get the "id" achievement.
     *
     * @param id the id of the achievement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the achievement, or with status 404 (Not Found)
     */
    @GetMapping("/achievements/{id}")
    @Timed
    public ResponseEntity<Achievement> getAchievement(@PathVariable Long id) {
        log.debug("REST request to get Achievement : {}", id);
        Achievement achievement = achievementRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(achievement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /achievements/:id : delete the "id" achievement.
     *
     * @param id the id of the achievement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/achievements/{id}")
    @Timed
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        log.debug("REST request to delete Achievement : {}", id);
        achievementRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("achievement", id.toString())).build();
    }

}
