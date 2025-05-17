package com.licenta.licenta.business.form.service;

import com.licenta.licenta.business.form.dto.FormDTO;
import com.licenta.licenta.business.form.mapper.FormMapper;
import com.licenta.licenta.business.form.model.Form;
import com.licenta.licenta.business.form.repository.FormRepository;
import com.licenta.licenta.business.organisation.service.OrganisationService;
import com.licenta.licenta.security.mapper.UserMapper;
import com.licenta.licenta.security.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepository;
    private final OrganisationService organisationService;
    private final FormMapper formMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public List<FormDTO> getAllByOrganisationId(Long organisationId) {
        return formRepository.findAllByOrganisation(organisationService.getOrganisationById(organisationId)).stream()
                .map(formMapper::toDTO).toList();
    }

    public FormDTO getFormById(Long id) {
        Form form = formRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Form not found"));
        return formMapper.toDTO(form);
    }

    public FormDTO saveForm(FormDTO formDTO) {
        Form form = formDTO.getPages() != null ? formMapper.toEntity(formDTO) : createEmptyForm(formDTO);
        form = formRepository.save(form);
        return formDTO.getPages() != null ? formMapper.toDTO(form) : getEmptyFormDTO(form);
    }

    private Form createEmptyForm(FormDTO formDTO) {
        Form form = new Form();
        form.setOrganisation(organisationService.getOrganisationById(formDTO.getOrganisationId()));
        form.setLastModifiedBy(userService.getUserById(Long.valueOf(formDTO.getLastModifiedById())));
        form.setLastModifiedAt(formDTO.getLastModifiedAt());
        return form;
    }

    private FormDTO getEmptyFormDTO(Form form) {
        FormDTO formDTO = new FormDTO();
        formDTO.setOrganisationId(form.getOrganisation().getId());
        formDTO.setLastModifiedById(String.valueOf(form.getLastModifiedBy().getId()));
        formDTO.setLastModifiedBy(userMapper.toDTO(form.getLastModifiedBy()));
        formDTO.setLastModifiedAt(formDTO.getLastModifiedAt());

        return formDTO;
    }

    public void deleteByFormId(Long formId) {
        formRepository.deleteById(formId);
    }
}
