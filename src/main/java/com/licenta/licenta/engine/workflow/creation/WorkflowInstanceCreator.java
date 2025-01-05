package com.licenta.licenta.engine.workflow.creation;

import com.licenta.licenta.engine.workflow.Workflow;
import com.licenta.licenta.engine.workflow.WorkflowComponent;
import com.licenta.licenta.engine.workflow.WorkflowComponentType;
import com.licenta.licenta.engine.workflow.components.StartTask;
import com.licenta.licenta.engine.workflow.service.WorkflowInstanceCreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkflowInstanceCreator {
    private final WorkflowInstanceCreatorService workflowInstanceCreatorService;

    public Workflow createWorkflow(/*TODO workflow type(id, name)*/) {
        Workflow workflow = new Workflow();

        //TODO retrieve wf definition

        StartTask startTask = new StartTask();
        workflow.setStartTask(startTask);
        List<WorkflowComponentType> components = List.of(
                WorkflowComponentType.STORE_DOCUMENTS_TASK, WorkflowComponentType.WAIT_FOR_APPROVAL_TASK,
                WorkflowComponentType.SEND_EMAIL_TASK, WorkflowComponentType.END_TASK);

        WorkflowComponent previousComponent = startTask;
        for (WorkflowComponentType componentType : components) {
            WorkflowComponent component = workflowInstanceCreatorService.getWorkflowComponentByType(componentType);

            //TODO set parameters for component

            previousComponent.setNext(component);
            previousComponent = component;
        }

        return workflow;
    }
}
