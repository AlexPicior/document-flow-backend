package com.licenta.licenta.business.organisation.service;

import com.licenta.licenta.business.organisation.model.Organisation;
import com.licenta.licenta.business.organisation.repository.OrganisationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganisationService {
    private final OrganisationRepository organisationRepository;

    public Organisation getOrganisationById(Long id) {
        return organisationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organisation with id " + id + " not found"));
    }
}
