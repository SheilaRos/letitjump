package com.bprogramers.letitjump.repository;

import com.bprogramers.letitjump.domain.UserCustomAtributes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserCustomAtributes entity.
 */
@SuppressWarnings("unused")
public interface UserCustomAtributesRepository extends JpaRepository<UserCustomAtributes,Long> {
    @Query("SELECT u FROM UserCustomAtributes  u ORDER BY u.score DESC")
    List<UserCustomAtributes> findAllOrderByScore();

    UserCustomAtributes findByUserLogin(String login);
}
