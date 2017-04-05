package com.bprogramers.letitjump.repository;

import com.bprogramers.letitjump.domain.Map;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Map entity.
 */
@SuppressWarnings("unused")
public interface MapRepository extends JpaRepository<Map,Long> {

}
