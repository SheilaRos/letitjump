package com.bprogramers.letitjump.repository;

import com.bprogramers.letitjump.domain.Skins;
import com.bprogramers.letitjump.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Skins entity.
 */
@SuppressWarnings("unused")
public interface SkinsRepository extends JpaRepository<Skins,Long> {

    @Query("select distinct skins from Skins skins left join fetch skins.users")
    List<Skins> findAllWithEagerRelationships();

    @Query("select skins from Skins skins left join fetch skins.users where skins.id =:id")
    Skins findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select skin from Skins skin where :user member of skin.users")
    List<Skins> findByUser(@Param("user") User user);
}
