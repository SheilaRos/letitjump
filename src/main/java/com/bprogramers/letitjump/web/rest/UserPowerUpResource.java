package com.bprogramers.letitjump.web.rest;

import com.bprogramers.letitjump.domain.PowerUp;
import com.bprogramers.letitjump.domain.User;
import com.bprogramers.letitjump.domain.UserCustomAtributes;
import com.bprogramers.letitjump.domain.UserPowerUp;
import com.bprogramers.letitjump.repository.PowerUpRepository;
import com.bprogramers.letitjump.repository.UserCustomAtributesRepository;
import com.bprogramers.letitjump.repository.UserPowerUpRepository;
import com.bprogramers.letitjump.repository.UserRepository;
import com.bprogramers.letitjump.security.SecurityUtils;
import com.bprogramers.letitjump.web.rest.util.HeaderUtil;
import com.bprogramers.letitjump.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserPowerUp.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserPowerUpResource {

    private final Logger log = LoggerFactory.getLogger(UserPowerUpResource.class);

    @Inject
    private PowerUpRepository powerUpRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private UserPowerUpRepository userPowerUpRepository;
    @Inject
    private UserCustomAtributesRepository userCustomAtributesRepository;


    /**
     * POST  /user-power-ups : Create a new userPowerUp.
     *
     * @param userPowerUp the userPowerUp to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userPowerUp, or with status 400 (Bad Request) if the userPowerUp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-power-ups")
    @Timed
    public ResponseEntity<UserPowerUp> createUserPowerUp(@Valid @RequestBody UserPowerUp userPowerUp) throws URISyntaxException {
        log.debug("REST request to save UserPowerUp : {}", userPowerUp);
        if (userPowerUp.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userPowerUp", "idexists", "A new userPowerUp cannot already have an ID")).body(null);
        }
        UserPowerUp result = userPowerUpRepository.save(userPowerUp);
        return ResponseEntity.created(new URI("/api/user-power-ups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userPowerUp", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-power-ups : Updates an existing userPowerUp.
     *
     * @param userPowerUp the userPowerUp to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userPowerUp,
     * or with status 400 (Bad Request) if the userPowerUp is not valid,
     * or with status 500 (Internal Server Error) if the userPowerUp couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-power-ups")
    @Timed
    public ResponseEntity<UserPowerUp> updateUserPowerUp(@Valid @RequestBody UserPowerUp userPowerUp) throws URISyntaxException {
        log.debug("REST request to update UserPowerUp : {}", userPowerUp);
        if (userPowerUp.getId() == null) {
            return createUserPowerUp(userPowerUp);
        }
        UserPowerUp result = userPowerUpRepository.save(userPowerUp);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userPowerUp", userPowerUp.getId().toString()))
            .body(result);
    }


    /** */
    @PutMapping("/user-power-ups/{idPowerUp}/{quantity}/byPriceGame")
    @Timed
    public ResponseEntity<UserPowerUp> addPowerUpGame(@PathVariable Long idPowerUp, @PathVariable int quantity) throws URISyntaxException {
        log.debug("REST request to buy Power Up : {}", idPowerUp);
        PowerUp powerUp = powerUpRepository.findOne(idPowerUp);
        if(powerUp == null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("powerUp", "idPowerUpNotExist", "...")).body(null);
        }
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        UserCustomAtributes userCustomAtributes = userCustomAtributesRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin());

        if(userCustomAtributes.getMoneyGame()<(powerUp.getPriceGame()*quantity)){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("powerUp", "idPowerUpNotEnoughtMoneyGame", "...")).body(null);
        }

        UserPowerUp userPowerUp = new UserPowerUp(0, user, powerUp);

        if(userPowerUpRepository.findByUser(user)
            .map(UserPowerUp::getPowerUp)
            .anyMatch(powerUp1 -> powerUp1.equals(powerUp))){
            userPowerUp = userPowerUpRepository.findByUserAndPowerUp(user, powerUp);
        }

        userPowerUp.setQuantity(userPowerUp.getQuantity()+quantity);
        userPowerUpRepository.save(userPowerUp);

        userCustomAtributes.setMoneyGame(userCustomAtributes.getMoneyGame()-(powerUp.getPriceGame()*quantity));
        userCustomAtributesRepository.save(userCustomAtributes);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("powerUp", powerUp.getId().toString()))
            .body(userPowerUp);
    }
    /** */
    @PutMapping("/user-power-ups/{idPowerUp}/{quantity}/byPricePremium")
    @Timed
    public ResponseEntity<UserPowerUp> addPowerUpPremium(@PathVariable Long idPowerUp, @PathVariable int quantity) throws URISyntaxException {
        log.debug("REST request to buy Power Up : {}", idPowerUp);
        PowerUp powerUp = powerUpRepository.findOne(idPowerUp);
        if(powerUp == null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("powerUp", "idPowerUpNotExist", "...")).body(null);
        }
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        UserCustomAtributes userCustomAtributes = userCustomAtributesRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin());

        if(userCustomAtributes.getMoneyPremium()<(powerUp.getPricePremium()*quantity)){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("powerUp", "idPowerUpNotEnoughtMoneyPremium", "...")).body(null);
        }
        UserPowerUp userPowerUp = new UserPowerUp(0, user, powerUp);

        if(userPowerUpRepository.findByUser(user)
            .map(UserPowerUp::getPowerUp)
            .anyMatch(powerUp1 -> powerUp1.equals(powerUp))){
            userPowerUp = userPowerUpRepository.findByUserAndPowerUp(user, powerUp);
        }

        userPowerUp.setQuantity(userPowerUp.getQuantity()+quantity);
        userPowerUpRepository.save(userPowerUp);
        userCustomAtributes.setMoneyPremium(userCustomAtributes.getMoneyPremium()-(powerUp.getPricePremium()*quantity));
        userCustomAtributesRepository.save(userCustomAtributes);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("powerUp", powerUp.getId().toString()))
            .body(userPowerUp);
    }


    /**
     * GET  /user-power-ups : get all the userPowerUps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userPowerUps in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/user-power-ups")
    @Timed
    public ResponseEntity<List<UserPowerUp>> getAllUserPowerUps(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserPowerUps");
        Page<UserPowerUp> page = userPowerUpRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-power-ups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/user-power-up/byUser/")
    @Timed
    public List<UserPowerUp> getAllPowerUpByUser() {
        log.debug("REST request to get all PowerUp by user");
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        List <UserPowerUp> userPowerUps = (List<UserPowerUp>) userPowerUpRepository.findByUser(user);
        return userPowerUps;
    }

    /**
     * GET  /user-power-ups/:id : get the "id" userPowerUp.
     *
     * @param id the id of the userPowerUp to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userPowerUp, or with status 404 (Not Found)
     */
    @GetMapping("/user-power-ups/{id}")
    @Timed
    public ResponseEntity<UserPowerUp> getUserPowerUp(@PathVariable Long id) {
        log.debug("REST request to get UserPowerUp : {}", id);
        UserPowerUp userPowerUp = userPowerUpRepository.findOne(id);
        return Optional.ofNullable(userPowerUp)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-power-ups/:id : delete the "id" userPowerUp.
     *
     * @param id the id of the userPowerUp to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-power-ups/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserPowerUp(@PathVariable Long id) {
        log.debug("REST request to delete UserPowerUp : {}", id);
        userPowerUpRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userPowerUp", id.toString())).build();
    }

}
