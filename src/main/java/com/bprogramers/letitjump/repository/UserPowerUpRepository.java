package com.bprogramers.letitjump.repository;

import com.bprogramers.letitjump.domain.PowerUp;
import com.bprogramers.letitjump.domain.User;
import com.bprogramers.letitjump.domain.UserPowerUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UserPowerUp entity.
 */
@SuppressWarnings("unused")
public interface UserPowerUpRepository extends JpaRepository<UserPowerUp,Long> {

    @Query("select userPowerUp from UserPowerUp userPowerUp where userPowerUp.user.login = ?#{principal.username}")
    List<UserPowerUp> findByUserIsCurrentUser();

    UserPowerUp findByUserAndPowerUp(User user, PowerUp powerUp);

    @Query("select userPowerUp from UserPowerUp userPowerUp where userPowerUp.user = :user")
    List<UserPowerUp> findByUser(@Param("user") User user);
}
