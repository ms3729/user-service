package com.rata.userService.repositories.mysql;

import com.rata.userService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByPartyId(Long partyId);

    boolean existsByUsername(String username);

    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.party p
            LEFT JOIN FETCH p.person pp
            LEFT JOIN FETCH p.personTranslations
            LEFT JOIN FETCH p.organizationTranslations
            WHERE u.username = :username
            """)
    Optional<User> findByUsernameWithParty(@Param("username") String username);

    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.party p
            LEFT JOIN FETCH p.person pp
            LEFT JOIN FETCH p.personTranslations
            LEFT JOIN FETCH p.organizationTranslations
            WHERE u.id = :id
            """)
    Optional<User> findByIdWithParty(@Param("id") long id);

    @Query("select u from User u where u.password is null ")
    List<User> findAllWithoutPassword();
}