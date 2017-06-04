package com.bprogramers.letitjump.web.rest;

import com.bprogramers.letitjump.domain.Skins;
import com.bprogramers.letitjump.domain.User;
import com.bprogramers.letitjump.domain.UserCustomAtributes;
import com.bprogramers.letitjump.repository.SkinsRepository;
import com.bprogramers.letitjump.repository.UserCustomAtributesRepository;
import com.bprogramers.letitjump.repository.UserRepository;
import com.bprogramers.letitjump.security.SecurityUtils;
import com.bprogramers.letitjump.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Skins.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SkinsResource {

    private final Logger log = LoggerFactory.getLogger(SkinsResource.class);

    @Inject
    private SkinsRepository skinsRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private UserCustomAtributesRepository userCustomAtributesRepository;

    /**
     * POST  /skins : Create a new skins.
     *
     * @param skins the skins to create
     * @return the ResponseEntity with status 201 (Created) and with body the new skins, or with status 400 (Bad Request) if the skins has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/skins")
    @Timed
    public ResponseEntity<Skins> createSkins(@Valid @RequestBody Skins skins) throws URISyntaxException {
        log.debug("REST request to save Skins : {}", skins);
        if (skins.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("skins", "idexists", "A new skins cannot already have an ID")).body(null);
        }
        Skins result = skinsRepository.save(skins);
        return ResponseEntity.created(new URI("/api/skins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("skins", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /skins : Updates an existing skins.
     *
     * @param skins the skins to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated skins,
     * or with status 400 (Bad Request) if the skins is not valid,
     * or with status 500 (Internal Server Error) if the skins couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/skins")
    @Timed
    public ResponseEntity<Skins> updateSkins(@Valid @RequestBody Skins skins) throws URISyntaxException {
        log.debug("REST request to update Skins : {}", skins);
        if (skins.getId() == null) {
            return createSkins(skins);
        }
        Skins result = skinsRepository.save(skins);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("skins", skins.getId().toString()))
            .body(result);
    }

    /***/
    @PutMapping("/skins/{idSkin}/byPriceGame")
    @Timed
    public ResponseEntity<Skins> addSkinToUser(@PathVariable Long idSkin) throws URISyntaxException {
        log.debug("REST request .... : {}", idSkin);

        Skins skin = skinsRepository.findOneWithEagerRelationships(idSkin);

        if(skin == null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("skins", "idSkinNotExist", "...")).body(null);
        }
        //para pillar el user que ha hecho la petición
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        UserCustomAtributes userCustomAtributes = userCustomAtributesRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin());

        if(userCustomAtributes.getMoneyGame()<skin.getPriceGame()){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("skins", "idSkinNotEnoughtMoney", "...")).body(null);
        }

        if(skinsRepository.findByUser(user).contains(skin)){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("skins", "idSkinAlreadyInUser", "...")).body(null);
        }

        userCustomAtributes.setMoneyGame(userCustomAtributes.getMoneyGame()-skin.getPriceGame());
        skin.addUser(user);
        userCustomAtributesRepository.save(userCustomAtributes);
        Skins result = skinsRepository.save(skin);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("skins", skin.getId().toString()))
            .body(result);
    }

    /***/
    @PutMapping("/skins/{idSkin}/byPricePremium")
    @Timed
    public ResponseEntity<Skins> addSkinWithPremiumToUser(@PathVariable Long idSkin) throws URISyntaxException {
        log.debug("REST request .... : {}", idSkin);

        Skins skin = skinsRepository.findOneWithEagerRelationships(idSkin);

        if(skin == null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("skins", "idSkinNotExist", "...")).body(null);
        }
        //para pillar el user que ha hecho la petición
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        UserCustomAtributes userCustomAtributes = userCustomAtributesRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin());

        if(userCustomAtributes.getMoneyPremium()<skin.getPricePremium()){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("skins", "idSkinNotEnoughtMoneyPremium", "...")).body(null);
        }

        if(skinsRepository.findByUser(user).contains(skin)){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("skins", "idSkinAlreadyInUser", "...")).body(null);
        }

        userCustomAtributes.setMoneyPremium(userCustomAtributes.getMoneyPremium()-skin.getPricePremium());
        skin.addUser(user);
        userCustomAtributesRepository.save(userCustomAtributes);
        Skins result = skinsRepository.save(skin);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("skins", skin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /skins : get all the skins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of skins in body
     */
    @GetMapping("/skins")
    @Timed
    public List<Skins> getAllSkins() {
        log.debug("REST request to get all Skins");
        List<Skins> skins = skinsRepository.findAllWithEagerRelationships();
        return skins;
    }

    @GetMapping("/skins/byUser/")
    @Timed
    public List<Skins> getAllSkinsByUser() {
        log.debug("REST request to get all Skins by user");
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        List<Skins> skins = skinsRepository.findByUser(user);
        return skins;
    }

    /**
     * GET  /skins/:id : get the "id" skins.
     *
     * @param id the id of the skins to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the skins, or with status 404 (Not Found)
     */
    @GetMapping("/skins/{id}")
    @Timed
    public ResponseEntity<Skins> getSkins(@PathVariable Long id) {
        log.debug("REST request to get Skins : {}", id);
        Skins skins = skinsRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(skins)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /skins/:id : delete the "id" skins.
     *
     * @param id the id of the skins to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/skins/{id}")
    @Timed
    public ResponseEntity<Void> deleteSkins(@PathVariable Long id) {
        log.debug("REST request to delete Skins : {}", id);
        skinsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("skins", id.toString())).build();
    }

}
