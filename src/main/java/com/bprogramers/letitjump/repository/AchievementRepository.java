package com.bprogramers.letitjump.repository;

import com.bprogramers.letitjump.domain.Achievement;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Achievement entity.
 */
@SuppressWarnings("unused")
public interface AchievementRepository extends JpaRepository<Achievement,Long> {

    @Query("select distinct achievement from Achievement achievement left join fetch achievement.users")
    List<Achievement> findAllWithEagerRelationships();

    @Query("select achievement from Achievement achievement left join fetch achievement.users where achievement.id =:id")
    Achievement findOneWithEagerRelationships(@Param("id") Long id);

}
