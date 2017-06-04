package com.bprogramers.letitjump.repository;

import com.bprogramers.letitjump.domain.Answer;

import com.bprogramers.letitjump.domain.ForumEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Answer entity.
 */
@SuppressWarnings("unused")
public interface AnswerRepository extends JpaRepository<Answer,Long> {

    @Query("SELECT  answer from Answer answer where answer.forumEntry = :entry")
    List<Answer> findByForumEntry(@Param("user") ForumEntry entry);

}
