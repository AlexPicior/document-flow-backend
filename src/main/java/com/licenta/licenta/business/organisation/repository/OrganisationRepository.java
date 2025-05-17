package com.licenta.licenta.business.organisation.repository;

import com.licenta.licenta.business.organisation.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
}
