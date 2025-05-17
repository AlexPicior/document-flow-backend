package com.licenta.licenta.business.form.mapper;

import com.licenta.licenta.business.form.dto.FormFieldRecordDTO;
import com.licenta.licenta.business.form.dto.FormRecordDTO;
import com.licenta.licenta.business.form.model.Form;
import com.licenta.licenta.business.form.model.FormFieldRecord;
import com.licenta.licenta.business.form.model.FormFieldRecordArrayValue;
import com.licenta.licenta.business.form.model.FormRecord;
import com.licenta.licenta.business.form.type.FormRecordType;
import com.licenta.licenta.business.organisation.service.OrganisationService;
import com.licenta.licenta.security.mapper.UserMapper;
import com.licenta.licenta.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class FormRecordMapper {
    private final FormMapper formMapper;
    private final UserService userService;
    private final OrganisationService organisationService;
    private final UserMapper userMapper;

    public List<FormRecord> allToEntity(List<FormRecordDTO> formRecordDTOList) {
        return formRecordDTOList.stream().map(this::toEntity).toList();
    }

    public List<FormRecordDTO> allToDto(List<FormRecord> formRecordList) {
        return formRecordList.stream().map(this::toDTO).toList();
    }

    public FormRecord toEntity(FormRecordDTO formRecordDTO) {
        FormRecord formRecord = new FormRecord();
        formRecord.setId(formRecordDTO.getId());
        formRecord.setCompletedBy(userService.getUserById(formRecordDTO.getUserId()));
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        formRecord.setCompletedAt(formRecordDTO.getCompletedAt() != null ? LocalDate.parse(formRecordDTO.getCompletedAt(), inputFormatter) : null);
        Form form = formMapper.toEntity(formRecordDTO.getForm());
        formRecord.setForm(form);
        List<FormFieldRecord> formFieldRecords = new ArrayList<>();
        formRecordDTO.getFieldRecords().stream()
                .map(formFieldRecordDTO -> formFieldRecordDTOToEntity(formFieldRecordDTO, formRecord, form))
                .forEach(formFieldRecords::add);
        formRecord.setFormFieldRecords(formFieldRecords);
        formRecord.setFormRecordType(formRecordDTO.getFormRecordType() != null ? FormRecordType.fromString(formRecordDTO.getFormRecordType()) : null);
        formRecord.setOrganisation(formRecordDTO.getOrganisationId() != null ? organisationService.getOrganisationById(formRecordDTO.getOrganisationId()) : null);
        return formRecord;
    }

    private FormFieldRecord formFieldRecordDTOToEntity(FormFieldRecordDTO formFieldRecordDTO, FormRecord formRecord, Form form) {
        FormFieldRecord formFieldRecord = new FormFieldRecord();
        formFieldRecord.setId(formFieldRecordDTO.getId());
        formFieldRecord.setValue(formFieldRecordDTO.getValue());
        List<FormFieldRecordArrayValue> arrayValues = new ArrayList<>();
        formFieldRecordDTO.getArrayValues().forEach(value -> {
            FormFieldRecordArrayValue formFieldRecordArrayValue = new FormFieldRecordArrayValue();
            formFieldRecordArrayValue.setValue(value);
            formFieldRecordArrayValue.setFormFieldRecord(formFieldRecord);
            arrayValues.add(formFieldRecordArrayValue);
        });
        formFieldRecord.setArrayValues(arrayValues);
        formFieldRecord.setFormRecord(formRecord);
        formFieldRecord.setFormField(formMapper.formFieldDTOToEntity(formFieldRecordDTO.getFormField(), form));
        return formFieldRecord;
    }

    public FormRecordDTO toDTO(FormRecord formRecord) {
        FormRecordDTO formRecordDTO = new FormRecordDTO();
        formRecordDTO.setId(formRecord.getId());
        formRecordDTO.setUserId(formRecord.getCompletedBy().getId());
        formRecordDTO.setCompletedBy(userMapper.toDTO(formRecord.getCompletedBy()));
        formRecordDTO.setCompletedAt(String.valueOf(formRecord.getCompletedAt()));
        formRecordDTO.setForm(formMapper.toDTO(formRecord.getForm()));
        formRecordDTO.setFormId(formRecord.getForm().getId());
        formRecordDTO.setFieldRecords(formRecord.getFormFieldRecords().stream().map(this::formFieldRecordToDTO).toList());
        formRecordDTO.setFormRecordType(formRecord.getFormRecordType() != null ? formRecord.getFormRecordType().getLabel() : null);
        formRecordDTO.setOrganisationId(formRecord.getOrganisation() != null ? formRecord.getOrganisation().getId() : null);
        return formRecordDTO;
    }

    private FormFieldRecordDTO formFieldRecordToDTO(FormFieldRecord formFieldRecord) {
        FormFieldRecordDTO formFieldRecordDTO = new FormFieldRecordDTO();
        formFieldRecordDTO.setId(formFieldRecord.getId());
        formFieldRecordDTO.setValue(formFieldRecord.getValue());
        formFieldRecordDTO.setArrayValues(formFieldRecord.getArrayValues().stream()
                .map(FormFieldRecordArrayValue::getValue).toList());
        formFieldRecordDTO.setFormField(formMapper.formFieldToDTO(formFieldRecord.getFormField()));
        return formFieldRecordDTO;
    }
}
