package com.bprogramers.letitjump.repository;

import com.bprogramers.letitjump.domain.ForumEntry;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ForumEntry entity.
 */
@SuppressWarnings("unused")
public interface ForumEntryRepository extends JpaRepository<ForumEntry,Long> {

    @Query("select forumEntry from ForumEntry forumEntry where forumEntry.user.login = ?#{principal.username}")
    List<ForumEntry> findByUserIsCurrentUser();

}
