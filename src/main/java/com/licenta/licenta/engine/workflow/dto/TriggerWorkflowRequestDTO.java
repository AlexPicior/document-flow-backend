package com.licenta.licenta.engine.workflow.dto;

import com.licenta.licenta.business.form.dto.FormRecordDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TriggerWorkflowRequestDTO {
    private String personKey;
    private String formKey;
    private FormRecordDTO formRecord;

    public TriggerWorkflowRequestDTO(TriggerWorkflowRequestDTO triggerWorkflowRequestDTO) {
        this.personKey = triggerWorkflowRequestDTO.getPersonKey();
        this.formKey = triggerWorkflowRequestDTO.getFormKey();
        this.formRecord = triggerWorkflowRequestDTO.getFormRecord();
    }
}
