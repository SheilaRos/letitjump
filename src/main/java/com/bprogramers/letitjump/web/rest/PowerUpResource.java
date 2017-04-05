package com.bprogramers.letitjump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bprogramers.letitjump.domain.PowerUp;

import com.bprogramers.letitjump.repository.PowerUpRepository;
import com.bprogramers.letitjump.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PowerUp.
 */
@RestController
@RequestMapping("/api")
public class PowerUpResource {

    private final Logger log = LoggerFactory.getLogger(PowerUpResource.class);
        
    @Inject
    private PowerUpRepository powerUpRepository;

    /**
     * POST  /power-ups : Create a new powerUp.
     *
     * @param powerUp the powerUp to create
     * @return the ResponseEntity with status 201 (Created) and with body the new powerUp, or with status 400 (Bad Request) if the powerUp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/power-ups")
    @Timed
    public ResponseEntity<PowerUp> createPowerUp(@Valid @RequestBody PowerUp powerUp) throws URISyntaxException {
        log.debug("REST request to save PowerUp : {}", powerUp);
        if (powerUp.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("powerUp", "idexists", "A new powerUp cannot already have an ID")).body(null);
        }
        PowerUp result = powerUpRepository.save(powerUp);
        return ResponseEntity.created(new URI("/api/power-ups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("powerUp", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /power-ups : Updates an existing powerUp.
     *
     * @param powerUp the powerUp to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated powerUp,
     * or with status 400 (Bad Request) if the powerUp is not valid,
     * or with status 500 (Internal Server Error) if the powerUp couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/power-ups")
    @Timed
    public ResponseEntity<PowerUp> updatePowerUp(@Valid @RequestBody PowerUp powerUp) throws URISyntaxException {
        log.debug("REST request to update PowerUp : {}", powerUp);
        if (powerUp.getId() == null) {
            return createPowerUp(powerUp);
        }
        PowerUp result = powerUpRepository.save(powerUp);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("powerUp", powerUp.getId().toString()))
            .body(result);
    }

    /**
     * GET  /power-ups : get all the powerUps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of powerUps in body
     */
    @GetMapping("/power-ups")
    @Timed
    public List<PowerUp> getAllPowerUps() {
        log.debug("REST request to get all PowerUps");
        List<PowerUp> powerUps = powerUpRepository.findAll();
        return powerUps;
    }

    /**
     * GET  /power-ups/:id : get the "id" powerUp.
     *
     * @param id the id of the powerUp to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the powerUp, or with status 404 (Not Found)
     */
    @GetMapping("/power-ups/{id}")
    @Timed
    public ResponseEntity<PowerUp> getPowerUp(@PathVariable Long id) {
        log.debug("REST request to get PowerUp : {}", id);
        PowerUp powerUp = powerUpRepository.findOne(id);
        return Optional.ofNullable(powerUp)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /power-ups/:id : delete the "id" powerUp.
     *
     * @param id the id of the powerUp to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/power-ups/{id}")
    @Timed
    public ResponseEntity<Void> deletePowerUp(@PathVariable Long id) {
        log.debug("REST request to delete PowerUp : {}", id);
        powerUpRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("powerUp", id.toString())).build();
    }

}
