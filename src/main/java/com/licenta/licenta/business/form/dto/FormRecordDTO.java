package com.licenta.licenta.business.form.dto;

import com.licenta.licenta.business.form.type.FormRecordType;
import com.licenta.licenta.security.dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FormRecordDTO {
    private Long id;

    private Long formId;

    private FormDTO form;

    private Long userId;

    private UserDTO completedBy;

    private String completedAt;

    private List<FormFieldRecordDTO> fieldRecords;

    private String formRecordType;

    private Long organisationId;
}
