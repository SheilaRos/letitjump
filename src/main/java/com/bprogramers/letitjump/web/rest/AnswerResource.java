package com.bprogramers.letitjump.web.rest;

import com.bprogramers.letitjump.domain.ForumEntry;
import com.codahale.metrics.annotation.Timed;
import com.bprogramers.letitjump.domain.Answer;

import com.bprogramers.letitjump.repository.AnswerRepository;
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
 * REST controller for managing Answer.
 */
@RestController
@RequestMapping("/api")
public class AnswerResource {

    private final Logger log = LoggerFactory.getLogger(AnswerResource.class);

    @Inject
    private AnswerRepository answerRepository;

    /**
     * POST  /answers : Create a new answer.
     *
     * @param answer the answer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new answer, or with status 400 (Bad Request) if the answer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/answers")
    @Timed
    public ResponseEntity<Answer> createAnswer(@RequestBody Answer answer) throws URISyntaxException {
        log.debug("REST request to save Answer : {}", answer);
        if (answer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("answer", "idexists", "A new answer cannot already have an ID")).body(null);
        }
        Answer result = answerRepository.save(answer);
        return ResponseEntity.created(new URI("/api/answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("answer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /answers : Updates an existing answer.
     *
     * @param answer the answer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated answer,
     * or with status 400 (Bad Request) if the answer is not valid,
     * or with status 500 (Internal Server Error) if the answer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/answers")
    @Timed
    public ResponseEntity<Answer> updateAnswer(@RequestBody Answer answer) throws URISyntaxException {
        log.debug("REST request to update Answer : {}", answer);
        if (answer.getId() == null) {
            return createAnswer(answer);
        }
        Answer result = answerRepository.save(answer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("answer", answer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /answers : get all the answers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of answers in body
     */
    @GetMapping("/answers")
    @Timed
    public List<Answer> getAllAnswers() {
        log.debug("REST request to get all Answers");
        List<Answer> answers = answerRepository.findAll();
        return answers;
    }

    /**
     * GET  /answers/:id : get the "id" answer.
     *
     * @param id the id of the answer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the answer, or with status 404 (Not Found)
     */
    @GetMapping("/answers/{id}")
    @Timed
    public ResponseEntity<Answer> getAnswer(@PathVariable Long id) {
        log.debug("REST request to get Answer : {}", id);
        Answer answer = answerRepository.findOne(id);
        return Optional.ofNullable(answer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/answers/forumEntry/{id}")
    @Timed
    public List<Answer> getAnswerByForumEntry(@PathVariable ForumEntry forumEntry) {
        List<Answer> answers = answerRepository.findByForumEntry(forumEntry);
        return answers;
    }

    /**
     * DELETE  /answers/:id : delete the "id" answer.
     *
     * @param id the id of the answer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/answers/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        log.debug("REST request to delete Answer : {}", id);
        answerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("answer", id.toString())).build();
    }

}
