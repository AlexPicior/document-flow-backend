package com.licenta.licenta.engine.workflow.repository;

import com.licenta.licenta.engine.workflow.model.TriggerKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TriggerKeyRepository extends JpaRepository<TriggerKey, Long> {
    List<TriggerKey> findAllByPersonKeyAndFormKey(String personKey, String formKey);
}
