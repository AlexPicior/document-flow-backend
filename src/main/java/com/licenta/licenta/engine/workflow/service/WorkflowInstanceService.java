package com.licenta.licenta.engine.workflow.service;

import com.licenta.licenta.engine.workflow.WorkflowComponent;
import com.licenta.licenta.engine.workflow.WorkflowInstance;
import com.licenta.licenta.engine.workflow.components.*;
import com.licenta.licenta.engine.workflow.dto.TriggerWorkflowRequestDTO;
import com.licenta.licenta.engine.workflow.dto.WorkflowJSONDefinition;
import com.licenta.licenta.engine.workflow.dto.task.Edge;
import com.licenta.licenta.engine.workflow.dto.task.Node;
import com.licenta.licenta.engine.workflow.dto.task.TaskProperty;
import com.licenta.licenta.engine.workflow.model.TriggerKey;
import com.licenta.licenta.engine.workflow.repository.TriggerKeyRepository;
import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import com.licenta.licenta.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class WorkflowInstanceService {
    private final IWorkflowInstanceCachingService workflowInstanceCachingService;
    private final TriggerKeyRepository triggerKeyRepository;
    private final UserService userService;
    private ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public void triggerWorkflowInstances(TriggerWorkflowRequestDTO triggerWorkflowRequestDTO) {
        TriggerWorkflowRequestDTO triggerWorkflowRequestForClient = new TriggerWorkflowRequestDTO(triggerWorkflowRequestDTO);
        triggerWorkflowRequestForClient.setPersonKey("client");
        boolean isClient = userService.isClient(Long.valueOf(triggerWorkflowRequestDTO.getPersonKey()));
        List<WorkflowInstance> workflowInstances = workflowInstanceCachingService
                .getWorkflowInstanceFromCache(workflowInstanceCachingService
                        .getTriggerKeyFromRequestDTO(isClient ? triggerWorkflowRequestForClient : triggerWorkflowRequestDTO));
        if (workflowInstances == null) {
            workflowInstances = createAndCacheWorkflowInstances(isClient ? triggerWorkflowRequestForClient : triggerWorkflowRequestDTO);
        }

        if (!workflowInstances.isEmpty()) {
            workflowInstances.forEach(workflowInstance -> executorService.submit(() -> workflowInstance.start(
                    getInputParameters(workflowInstance.getStartTask(), triggerWorkflowRequestDTO))));
        }
    }

    private List<WorkflowInstance> createAndCacheWorkflowInstances(TriggerWorkflowRequestDTO triggerWorkflowRequestDTO) {
        List<WorkflowInstance> workflowInstances = new ArrayList<>();
        List<TriggerKey> triggerKeyList = triggerKeyRepository.findAllByPersonKeyAndFormKey(
                triggerWorkflowRequestDTO.getPersonKey(), triggerWorkflowRequestDTO.getFormKey());

        if (!triggerKeyList.isEmpty()) {
            triggerKeyList.forEach(triggerKey -> {
                WorkflowJSONDefinition jsonDefinition = triggerKey.getWorkflowDefinition().getJsonDefinition();
                List<Edge> edges = jsonDefinition.getEdges();
                List<Node> nodes = jsonDefinition.getNodes();
                Map<String, WorkflowComponent> workflowComponents = getWorkflowComponentsFromNodes(nodes);
                workflowInstances.add(createInstance(workflowComponents, edges));
            });
            workflowInstanceCachingService.cacheWorkflowInstance(workflowInstanceCachingService.getTriggerKeyFromRequestDTO(triggerWorkflowRequestDTO), workflowInstances);
        }
        return workflowInstances;
    }

    private WorkflowInstance createInstance(Map<String, WorkflowComponent> workflowComponents, List<Edge> edges) {
        WorkflowInstance workflowInstance = new WorkflowInstance();
        AtomicReference<StartTask> startTask = new AtomicReference<>();
        EndTask endTask = new EndTask();
        edges.forEach(edge -> {
            String sourceTaskId = edge.getSource();
            String targetTaskId = edge.getTarget();
            WorkflowComponent workflowComponent = workflowComponents.get(sourceTaskId);
            if (workflowComponent != null) {
                if (targetTaskId.equals(EndTask.TYPE.getLabel())) {
                    workflowComponent.setNext(endTask);
                } else {
                    if (edge.getSourceHandle() != null) {
                        if (workflowComponent instanceof ApprovalTask approvalTask) {
                            if (edge.getSourceHandle().equals(ApprovalTask.APPROVED)) {
                                approvalTask.setNextTaskForApproved(workflowComponents.get(targetTaskId));
                            } else if (edge.getSourceHandle().equals(ApprovalTask.REJECTED)) {
                                approvalTask.setNextTaskForRejected(workflowComponents.get(targetTaskId));
                            }
                        } else if (workflowComponent instanceof ConditionalTask conditionalTask) {
                            if (edge.getSourceHandle().equals(ConditionalTask.YES)) {
                                conditionalTask.setNextTaskForYes(workflowComponents.get(targetTaskId));
                            } else if (edge.getSourceHandle().equals(ConditionalTask.NO)) {
                                conditionalTask.setNextTaskForNo(workflowComponents.get(targetTaskId));
                            }
                        }
                    } else {
                        workflowComponent.setNext(workflowComponents.get(targetTaskId));
                        if (sourceTaskId.equals(StartTask.TYPE.getLabel()) && workflowComponent instanceof StartTask) {
                            startTask.set((StartTask) workflowComponent);
                        }
                    }
                }
            }
        });
        workflowInstance.setStartTask(startTask.get());
        workflowInstance.setEndTask(endTask);
        return workflowInstance;
    }

    private Map<String, WorkflowComponent> getWorkflowComponentsFromNodes(List<Node> nodes) {
        Map<String, WorkflowComponent> workflowComponents = new HashMap<>();
        nodes.forEach(node -> {
            if(node.getType().equals(WorkflowComponentType.END_TASK)) {
                return;
            }
            WorkflowComponent workflowComponent = getWorkflowComponentFromNode(node);
            if (workflowComponent != null) {
                workflowComponents.put(node.getId(), workflowComponent);
            }
        });
        return workflowComponents;
    }

    private WorkflowComponent getWorkflowComponentFromNode(Node node) {
        WorkflowComponent workflowComponent = null;
        Map<String, TaskProperty> properties = node.getData().getTask().getProperties();
        switch (node.getType()) {
            case START_TASK -> workflowComponent = new StartTask(properties);
            case APPROVAL_TASK -> workflowComponent = new ApprovalTask(properties);
            case COMPLETE_FORM_TASK -> workflowComponent = new CompleteFormTask(properties);
            case REQUEST_MODIFICATION_TASK -> workflowComponent = new RequestModificationTask(properties);
            case SEND_CONTENT_TO_TASK -> workflowComponent = new SendContentToTask(properties);
            case SEND_NOTIFICATION_TASK -> workflowComponent = new SendNotificationTask(properties);
            case SEND_EMAIL_TASK -> workflowComponent = new SendEmailTask(properties);
            case STORE_TASK -> workflowComponent = new StoreTask(properties);
            case CONDITIONAL_TASK -> workflowComponent = new ConditionalTask(properties);
        }
        return workflowComponent;
    }

    private Map<String, Object> getInputParameters(StartTask startTask, TriggerWorkflowRequestDTO triggerWorkflowRequestDTO) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(startTask.getSenderReferenceName(), triggerWorkflowRequestDTO.getPersonKey());
        parameters.put(startTask.getFormReferenceName(), triggerWorkflowRequestDTO.getFormRecord());
        return parameters;
    }

}
