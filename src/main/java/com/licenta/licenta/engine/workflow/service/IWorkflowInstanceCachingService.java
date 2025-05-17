package com.licenta.licenta.engine.workflow.service;

import com.licenta.licenta.engine.workflow.WorkflowInstance;
import com.licenta.licenta.engine.workflow.dto.TriggerWorkflowRequestDTO;
import com.licenta.licenta.engine.workflow.model.TriggerKey;

import java.util.List;

public interface IWorkflowInstanceCachingService {
    List<WorkflowInstance> getWorkflowInstanceFromCache(String triggerKey);
    void cacheWorkflowInstance(String triggerKey, List<WorkflowInstance> workflowInstances);
    public void clearWorkflowInstanceFromCache(String triggerKey);
    String getTriggerKeyFromRequestDTO(TriggerWorkflowRequestDTO triggerWorkflowRequestDTO);
    public String getTriggerKeyFromRequest(TriggerKey triggerKey);
    public String getTriggerKeyForClient(TriggerWorkflowRequestDTO triggerWorkflowRequestDTO);
}
