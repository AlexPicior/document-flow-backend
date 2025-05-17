package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.business.form.dto.FormRecordDTO;
import com.licenta.licenta.business.form.service.FormRecordService;
import com.licenta.licenta.business.form.type.FormRecordType;
import com.licenta.licenta.engine.workflow.AbstractWorkflowComponent;
import com.licenta.licenta.engine.workflow.dto.task.TaskProperty;
import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import com.licenta.licenta.helper.SpringContextHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class StoreTask extends AbstractWorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.STORE_TASK;

    public static final String CONTENT = "content";

    private List<String> content;

    public StoreTask(Map<String, TaskProperty> properties) {
        this.content = (List<String>) properties.get(CONTENT).getValue();
    }

    @Override
    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        FormRecordService formRecordService = SpringContextHelper.getBean(FormRecordService.class);
        List<FormRecordDTO> contents = content.stream()
                .map(contentVariableName -> {
                    FormRecordDTO contentFormRecord = (FormRecordDTO)inputParameters.get(contentVariableName);
                    contentFormRecord.setOrganisationId(contentFormRecord.getForm().getOrganisationId());
                    contentFormRecord.setFormRecordType(FormRecordType.STORE.getLabel());
                    return contentFormRecord;
                }).toList();
        formRecordService.saveAll(contents);
//        System.out.println("Properties of " + TYPE + ": " + CONTENT + ": " + String.join(",", content));
        return inputParameters;
    }
}
