package com.bprogramers.letitjump.repository;

import com.bprogramers.letitjump.domain.UserPowerUp;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserPowerUp entity.
 */
@SuppressWarnings("unused")
public interface UserPowerUpRepository extends JpaRepository<UserPowerUp,Long> {

    @Query("select userPowerUp from UserPowerUp userPowerUp where userPowerUp.user.login = ?#{principal.username}")
    List<UserPowerUp> findByUserIsCurrentUser();

}
