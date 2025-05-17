package com.licenta.licenta.engine.workflow.service;

import com.licenta.licenta.engine.workflow.WorkflowInstance;
import com.licenta.licenta.engine.workflow.dto.TriggerWorkflowRequestDTO;
import com.licenta.licenta.engine.workflow.model.TriggerKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class WorkflowInstanceCachingService implements IWorkflowInstanceCachingService {

    private final RedisTemplate<String, List<WorkflowInstance>> redisTemplate;
    private static final long CACHE_TTL_HOURS = 24;

    @Override
    public List<WorkflowInstance> getWorkflowInstanceFromCache(String triggerKey) {
        return redisTemplate.opsForValue().get(triggerKey);
    }

    @Override
    public void cacheWorkflowInstance(String triggerKey, List<WorkflowInstance> workflowInstances) {
        redisTemplate.opsForValue().set(triggerKey, workflowInstances, CACHE_TTL_HOURS, TimeUnit.HOURS);
    }

    @Override
    public void clearWorkflowInstanceFromCache(String triggerKey) {
        redisTemplate.delete(triggerKey);
    }

    @Override
    public String getTriggerKeyFromRequestDTO(TriggerWorkflowRequestDTO triggerWorkflowRequestDTO) {
        return triggerWorkflowRequestDTO.getPersonKey() + "." + triggerWorkflowRequestDTO.getFormKey();
    }

    @Override
    public String getTriggerKeyFromRequest(TriggerKey triggerKey) {
        return triggerKey.getPersonKey() + "." + triggerKey.getFormKey();
    }

    @Override
    public String getTriggerKeyForClient(TriggerWorkflowRequestDTO triggerWorkflowRequestDTO) {
        return "client" + "." + triggerWorkflowRequestDTO.getFormKey();
    }
}
