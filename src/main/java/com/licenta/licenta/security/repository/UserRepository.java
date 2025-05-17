package com.licenta.licenta.security.repository;

import com.licenta.licenta.security.model.Authority;
import com.licenta.licenta.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByOrganisationIdAndAuthority(Long organisationId, Authority authority);
    List<User> findAllByRoleIdIn(Set<Long> roleIds);
}
