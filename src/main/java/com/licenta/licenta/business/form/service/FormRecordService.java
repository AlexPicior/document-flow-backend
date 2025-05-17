package com.licenta.licenta.business.form.service;

import com.licenta.licenta.business.form.dto.FormDTO;
import com.licenta.licenta.business.form.dto.FormFieldRecordDTO;
import com.licenta.licenta.business.form.dto.FormRecordDTO;
import com.licenta.licenta.business.form.mapper.FormRecordMapper;
import com.licenta.licenta.business.form.model.Form;
import com.licenta.licenta.business.form.model.FormRecord;
import com.licenta.licenta.business.form.repository.FormRecordRepository;
import com.licenta.licenta.business.form.repository.FormRepository;
import com.licenta.licenta.business.form.type.FormRecordType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormRecordService {
    private final FormRecordRepository formRecordRepository;
    private final FormRecordMapper formRecordMapper;
    private final FormService formService;

    public List<FormRecordDTO> findAllForStorage(Long organisationId) {
        List<FormRecord> formRecords = formRecordRepository.findAllByOrganisationIdAndFormRecordType(organisationId, FormRecordType.STORE);
        return formRecordMapper.allToDto(formRecords);
    }

    public void deleteFormRecord(Long formRecordId) {
        formRecordRepository.deleteById(formRecordId);
    }

    public List<FormRecordDTO> saveAll(List<FormRecordDTO> formRecordDTOList) {
        List<FormRecord> formRecords = formRecordRepository.saveAll(formRecordMapper.allToEntity(formRecordDTOList));
        return formRecordMapper.allToDto(formRecords);
    }

    public FormRecordDTO createEmptyFormRecordDTOFromFormId(Long formId) {
        FormRecordDTO formRecordDTO = new FormRecordDTO();
        formRecordDTO.setFormId(formId);
        formRecordDTO.setUserId(null);
        formRecordDTO.setCompletedAt(null);
        FormDTO form = formService.getFormById(formId);
        formRecordDTO.setForm(form);

        List<FormFieldRecordDTO> formFieldRecordDTOS = new ArrayList<>();
        form.getPages().forEach(page -> page.forEach(formField -> {
            FormFieldRecordDTO formFieldRecordDTO = new FormFieldRecordDTO();
            formFieldRecordDTO.setFormField(formField);
            formFieldRecordDTO.setValue("");
            formFieldRecordDTO.setArrayValues(new ArrayList<>());
            formFieldRecordDTOS.add(formFieldRecordDTO);
        }));
        formRecordDTO.setFieldRecords(formFieldRecordDTOS);

        return formRecordDTO;
    }
}
