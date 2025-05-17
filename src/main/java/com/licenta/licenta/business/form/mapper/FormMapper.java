package com.licenta.licenta.business.form.mapper;

import com.licenta.licenta.business.form.dto.FormDTO;
import com.licenta.licenta.business.form.dto.FormFieldDTO;
import com.licenta.licenta.business.form.model.Form;
import com.licenta.licenta.business.form.model.FormField;
import com.licenta.licenta.business.form.model.FormFieldOption;
import com.licenta.licenta.business.organisation.service.OrganisationService;
import com.licenta.licenta.security.mapper.UserMapper;
import com.licenta.licenta.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FormMapper {
    private final OrganisationService organisationService;
    private final UserService userService;
    private final UserMapper userMapper;

    public Form toEntity(FormDTO formDTO) {
        Form form = new Form();
        form.setId(formDTO.getId());
        form.setLabel(formDTO.getLabel());
        form.setDescription(formDTO.getDescription());
        form.setLastModifiedAt(formDTO.getLastModifiedAt());
        form.setOrganisation(organisationService.getOrganisationById(formDTO.getOrganisationId()));
        form.setLastModifiedBy(userService.getUserById(Long.valueOf(formDTO.getLastModifiedById())));
        List<FormField> fields = new ArrayList<>();
        AtomicInteger pageIndex = new AtomicInteger();
        formDTO.getPages().forEach(pageFields -> {
            int pageNumber = pageIndex.getAndIncrement();
            AtomicInteger index = new AtomicInteger();
            pageFields.forEach(fieldDTO -> {
                FormField field = new FormField();
                field.setLabel(fieldDTO.getLabel());
                field.setType(fieldDTO.getType());
                field.setOptions(fieldDTO.getOptions() != null ? fieldDTO.getOptions().stream().map(option -> {
                    FormFieldOption formFieldOption = new FormFieldOption();
                    formFieldOption.setValue(option);
                    formFieldOption.setFormField(field);
                    return formFieldOption;
                }).toList() : null);
                field.setIndex(index.getAndIncrement());
                field.setPageNumber(pageNumber);
                field.setForm(form);

                fields.add(field);
            });
        });
        form.setFields(fields);

        return form;
    }

    private List<FormFieldOption> formFieldOptionDTOsToEntities(FormFieldDTO fieldDTO, FormField field) {
        return fieldDTO.getOptions() != null ? fieldDTO.getOptions().stream().map(option -> {
            FormFieldOption formFieldOption = new FormFieldOption();
            formFieldOption.setValue(option);
            formFieldOption.setFormField(field);
            return formFieldOption;
        }).toList() : null;
    }

    public FormDTO toDTO(Form form) {
        FormDTO formDTO = new FormDTO();
        formDTO.setId(form.getId());
        formDTO.setLabel(form.getLabel());
        formDTO.setOrganisationId(form.getOrganisation().getId());
        formDTO.setDescription(form.getDescription());
        formDTO.setLastModifiedAt(form.getLastModifiedAt());
        formDTO.setLastModifiedById(String.valueOf(form.getLastModifiedBy().getId()));
        formDTO.setLastModifiedBy(userMapper.toDTO(form.getLastModifiedBy()));

        if (form.getFields() != null && !form.getFields().isEmpty()) {
            Map<Integer, List<FormField>> grouped = form.getFields().stream()
                    .collect(Collectors.groupingBy(FormField::getPageNumber));

            Map<Integer, List<FormFieldDTO>> groupedDTO = new HashMap<>();
            grouped.forEach((index, fields) -> {
                List<FormFieldDTO> fieldDTOs = fields.stream()
                        .sorted(Comparator.comparing(FormField::getIndex))
                        .map(this::formFieldToDTO).toList();
                groupedDTO.put(index, fieldDTOs);
            });

            List<List<FormFieldDTO>> pages = groupedDTO.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(Map.Entry::getValue)
                    .toList();

            formDTO.setPages(pages);
        }

        return formDTO;
    }

    public FormFieldDTO formFieldToDTO(FormField field) {
        FormFieldDTO fieldDTO = new FormFieldDTO();
        fieldDTO.setId(field.getId());
        fieldDTO.setLabel(field.getLabel());
        fieldDTO.setType(field.getType());
        fieldDTO.setPageNumber(field.getPageNumber());
        fieldDTO.setIndex(field.getIndex());
        fieldDTO.setOptions(field.getOptions() != null ? field.getOptions().stream().map(FormFieldOption::getValue).toList() : null);
        return fieldDTO;
    }

    public FormField formFieldDTOToEntity(FormFieldDTO fieldDTO, Form form) {
        FormField formField = new FormField();
        formField.setId(fieldDTO.getId());
        formField.setLabel(fieldDTO.getLabel());
        formField.setType(fieldDTO.getType());
        formField.setPageNumber(fieldDTO.getPageNumber());
        formField.setIndex(fieldDTO.getIndex());
        formField.setOptions(formFieldOptionDTOsToEntities(fieldDTO, formField));
        formField.setForm(form);
        return formField;
    }
}
