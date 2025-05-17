package com.licenta.licenta.business.form.repository;

import com.licenta.licenta.business.form.model.Form;
import com.licenta.licenta.business.organisation.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findAllByOrganisation(Organisation organisation);
}
