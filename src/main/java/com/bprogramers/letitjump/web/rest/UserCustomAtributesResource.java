package com.bprogramers.letitjump.web.rest;

import com.bprogramers.letitjump.domain.User;
import com.bprogramers.letitjump.repository.UserRepository;
import com.bprogramers.letitjump.security.SecurityUtils;
import com.bprogramers.letitjump.web.rest.vm.ManagedUserVM;
import com.codahale.metrics.annotation.Timed;
import com.bprogramers.letitjump.domain.UserCustomAtributes;

import com.bprogramers.letitjump.repository.UserCustomAtributesRepository;
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
 * REST controller for managing UserCustomAtributes.
 */
@RestController
@RequestMapping("/api")
public class UserCustomAtributesResource {

    private final Logger log = LoggerFactory.getLogger(UserCustomAtributesResource.class);

    @Inject
    private UserCustomAtributesRepository userCustomAtributesRepository;
    @Inject
    private UserRepository userRepository;

    /**
     * POST  /user-custom-atributes : Create a new userCustomAtributes.
     *
     * @param userCustomAtributes the userCustomAtributes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userCustomAtributes, or with status 400 (Bad Request) if the userCustomAtributes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-custom-atributes")
    @Timed
    public ResponseEntity<UserCustomAtributes> createUserCustomAtributes(@Valid @RequestBody UserCustomAtributes userCustomAtributes) throws URISyntaxException {
        log.debug("REST request to save UserCustomAtributes : {}", userCustomAtributes);
        if (userCustomAtributes.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userCustomAtributes", "idexists", "A new userCustomAtributes cannot already have an ID")).body(null);
        }
        UserCustomAtributes result = userCustomAtributesRepository.save(userCustomAtributes);
        return ResponseEntity.created(new URI("/api/user-custom-atributes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userCustomAtributes", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-custom-atributes : Updates an existing userCustomAtributes.
     *
     * @param userCustomAtributes the userCustomAtributes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userCustomAtributes,
     * or with status 400 (Bad Request) if the userCustomAtributes is not valid,
     * or with status 500 (Internal Server Error) if the userCustomAtributes couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-custom-atributes")
    @Timed
    public ResponseEntity<UserCustomAtributes> updateUserCustomAtributes(@Valid @RequestBody UserCustomAtributes userCustomAtributes) throws URISyntaxException {
        log.debug("REST request to update UserCustomAtributes : {}", userCustomAtributes);
        if (userCustomAtributes.getId() == null) {
            return createUserCustomAtributes(userCustomAtributes);
        }
        UserCustomAtributes result = userCustomAtributesRepository.save(userCustomAtributes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userCustomAtributes", userCustomAtributes.getId().toString()))
            .body(result);
    }


    @PutMapping("/user-custom-atributes/score/{score}/")
    @Timed
    public ResponseEntity<UserCustomAtributes> updateUserCustomAtributesScore(@RequestBody ManagedUserVM managedUserVM, Integer score) throws URISyntaxException {
       User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
       UserCustomAtributes userCustomAtributes = userCustomAtributesRepository.findByUserLogin(user.getLogin());
        if (userCustomAtributes.getId() == null) {
            return createUserCustomAtributes(userCustomAtributes);
        }
        if(score > userCustomAtributes.getScore()){
            userCustomAtributes.setScore(score.longValue());
        }
        UserCustomAtributes result = userCustomAtributesRepository.save(userCustomAtributes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userCustomAtributes", userCustomAtributes.getId().toString()))
            .body(result);
    }
    /**
     * GET  /user-custom-atributes : get all the userCustomAtributes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userCustomAtributes in body
     */
    @GetMapping("/user-custom-atributes")
    @Timed
    public List<UserCustomAtributes> getAllUserCustomAtributes() {
        log.debug("REST request to get all UserCustomAtributes");
        List<UserCustomAtributes> userCustomAtributes = userCustomAtributesRepository.findAll();
        return userCustomAtributes;
    }

    @GetMapping("/user-custom-atributes/byUser")
    @Timed
    public UserCustomAtributes getUserCustomAtributes() {
        log.debug("REST request to get all UserCustomAtributes");
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        UserCustomAtributes userCustomAtributes = userCustomAtributesRepository.findByUserLogin(user.getLogin());
        return userCustomAtributes;
    }

    /**
     * GET  /user-custom-atributes/:id : get the "id" userCustomAtributes.
     *
     * @param id the id of the userCustomAtributes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userCustomAtributes, or with status 404 (Not Found)
     */
    @GetMapping("/user-custom-atributes/{id}")
    @Timed
    public ResponseEntity<UserCustomAtributes> getUserCustomAtributes(@PathVariable Long id) {
        log.debug("REST request to get UserCustomAtributes : {}", id);
        UserCustomAtributes userCustomAtributes = userCustomAtributesRepository.findOne(id);
        return Optional.ofNullable(userCustomAtributes)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-custom-atributes/:id : delete the "id" userCustomAtributes.
     *
     * @param id the id of the userCustomAtributes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-custom-atributes/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserCustomAtributes(@PathVariable Long id) {
        log.debug("REST request to delete UserCustomAtributes : {}", id);
        userCustomAtributesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userCustomAtributes", id.toString())).build();
    }



}
