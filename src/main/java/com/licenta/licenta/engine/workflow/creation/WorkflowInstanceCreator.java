package com.licenta.licenta.engine.workflow.creation;

import com.licenta.licenta.engine.workflow.service.WorkflowInstanceCreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkflowInstanceCreator {
    private final WorkflowInstanceCreatorService workflowInstanceCreatorService;

//    public WorkflowInstance createWorkflow(/*TODO workflow type(id, name)*/) {
//        WorkflowInstance workflowInstance = new WorkflowInstance();
//
//        //TODO retrieve wf definition
//
//        StartTask startTask = new StartTask();
//        workflowInstance.setStartTask(startTask);
//        List<WorkflowComponentType> components = List.of(
//                WorkflowComponentType.STORE_DOCUMENT_TASK, WorkflowComponentType.WAIT_FOR_APPROVAL_TASK,
//                WorkflowComponentType.SEND_EMAIL_TASK, WorkflowComponentType.END_TASK);
//
//        WorkflowComponent previousComponent = startTask;
//        for (WorkflowComponentType componentType : components) {
//            WorkflowComponent component = workflowInstanceCreatorService.getWorkflowComponentByType(componentType);
//
//            //TODO set parameters for component
//
//            previousComponent.setNext(component);
//            previousComponent = component;
//        }
//
//        return workflowInstance;
//    }
}
