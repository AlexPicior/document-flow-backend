package com.licenta.licenta.business.form.repository;

import com.licenta.licenta.business.form.model.FormRecord;
import com.licenta.licenta.business.form.type.FormRecordType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormRecordRepository extends JpaRepository<FormRecord, Long> {
    List<FormRecord> findAllByOrganisationIdAndFormRecordType(Long organisationId, FormRecordType formRecordType);
}
