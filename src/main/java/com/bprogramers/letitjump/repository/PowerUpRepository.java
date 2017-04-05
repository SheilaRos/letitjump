package com.bprogramers.letitjump.repository;

import com.bprogramers.letitjump.domain.PowerUp;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PowerUp entity.
 */
@SuppressWarnings("unused")
public interface PowerUpRepository extends JpaRepository<PowerUp,Long> {

}
