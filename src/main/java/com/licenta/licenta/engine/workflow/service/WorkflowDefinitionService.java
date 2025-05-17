package com.licenta.licenta.engine.workflow.service;

import com.licenta.licenta.business.organisation.service.OrganisationService;
import com.licenta.licenta.engine.workflow.components.StartTask;
import com.licenta.licenta.engine.workflow.model.TriggerKey;
import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import com.licenta.licenta.engine.workflow.dto.WorkflowDefinitionDTO;
import com.licenta.licenta.engine.workflow.exception.StartTaskNotFound;
import com.licenta.licenta.engine.workflow.mapper.WorkflowDefinitionMapper;
import com.licenta.licenta.engine.workflow.model.WorkflowDefinition;
import com.licenta.licenta.engine.workflow.dto.task.Node;
import com.licenta.licenta.engine.workflow.dto.task.TaskProperty;
import com.licenta.licenta.engine.workflow.repository.WorkflowDefinitionRepository;
import com.licenta.licenta.security.mapper.UserMapper;
import com.licenta.licenta.security.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkflowDefinitionService {
    private final WorkflowDefinitionRepository workflowDefinitionRepository;
    private final WorkflowDefinitionMapper workflowDefinitionMapper;
    private final OrganisationService organisationService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final WorkflowInstanceCachingService workflowInstanceCachingService;

    public WorkflowDefinitionDTO getWorkflowDefinitionById(Long id) {
        WorkflowDefinition workflowDefinition = workflowDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        return workflowDefinitionMapper.toDTO(workflowDefinition);
    }

    public List<WorkflowDefinitionDTO> getWorkflowDefinitionsByOrganizationId(Long organizationId) {
        List<WorkflowDefinition> workflowDefinitions = workflowDefinitionRepository.findAllByOrganisationId(organizationId);
        return workflowDefinitions.stream().map(workflowDefinitionMapper::toDTO).toList();
    }

    public void deleteWorkflowDefinitionById(Long id) {
        clearWorkflowInstanceFromCacheByWorkflowDefinitionId(id);
        workflowDefinitionRepository.deleteById(id);
    }

    private void clearWorkflowInstanceFromCacheByWorkflowDefinitionId(Long workflowDefinitionId) {
        WorkflowDefinition workflowDefinition = workflowDefinitionRepository.findById(workflowDefinitionId)
                .orElseThrow(() -> new EntityNotFoundException("Workflow definition with id " + workflowDefinitionId + " not found"));

        workflowDefinition.getTriggerKeys().forEach(triggerKey ->
                workflowInstanceCachingService.clearWorkflowInstanceFromCache(
                        workflowInstanceCachingService.getTriggerKeyFromRequest(triggerKey)));
    }

    public WorkflowDefinitionDTO createWorkflowDefinition(WorkflowDefinitionDTO workflowDefinitionDTO) {
        WorkflowDefinition workflowDefinition;
        if (workflowDefinitionDTO.getJsonDefinition() != null) {
            workflowDefinition = workflowDefinitionMapper.toEntity(workflowDefinitionDTO);
            setTriggerKeyFromWorkflowDefinition(workflowDefinition);
            if (workflowDefinitionDTO.getId() != null) {
                clearWorkflowInstanceFromCacheByWorkflowDefinitionId(workflowDefinitionDTO.getId());
            }
        } else {
            workflowDefinition = createEmptyWorkflowDefinition(workflowDefinitionDTO);
        }
        workflowDefinition = workflowDefinitionRepository.save(workflowDefinition);
        return workflowDefinitionDTO.getJsonDefinition() != null
                ? workflowDefinitionMapper.toDTO(workflowDefinition)
                : getEmptyWorkflowDefinitionDTO(workflowDefinition);
    }

    private WorkflowDefinition createEmptyWorkflowDefinition(WorkflowDefinitionDTO workflowDefinitionDTO) {
        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setOrganisation(organisationService.getOrganisationById(Long.valueOf(workflowDefinitionDTO.getOrganisationId())));
        workflowDefinition.setLastModifiedBy(userService.getUserById(Long.valueOf(workflowDefinitionDTO.getLastModifiedById())));
        workflowDefinition.setLastModifiedAt(workflowDefinitionDTO.getLastModifiedAt());
        return workflowDefinition;
    }

    private WorkflowDefinitionDTO getEmptyWorkflowDefinitionDTO(WorkflowDefinition workflowDefinition) {
        WorkflowDefinitionDTO workflowDefinitionDTO = new WorkflowDefinitionDTO();
        workflowDefinitionDTO.setId(workflowDefinition.getId());
        workflowDefinitionDTO.setOrganisationId(workflowDefinition.getOrganisation().getId().toString());
        workflowDefinitionDTO.setLastModifiedBy(userMapper.toDTO(workflowDefinition.getLastModifiedBy()));
        workflowDefinitionDTO.setLastModifiedAt(workflowDefinition.getLastModifiedAt());
        return workflowDefinitionDTO;
    }

    private void setTriggerKeyFromWorkflowDefinition(WorkflowDefinition workflowDefinition) {
        Node startTask = workflowDefinition.getJsonDefinition().getNodes()
                .stream()
                .filter(node -> node.getType().equals(WorkflowComponentType.START_TASK))
                .findFirst().orElseThrow(() -> new StartTaskNotFound("Start task not found"));
        Map<String, TaskProperty> properties = startTask.getData().getTask().getProperties();
        List<TriggerKey> triggerKeys = new ArrayList<>();
        Set<String> personKeys;
        String formKey = (String) properties.get(StartTask.FORM_TYPE).getValue();

        String personType = (String) properties.get(StartTask.TRIGGERED_BY).getValue();
        if (personType.equals(StartTask.CLIENT)) {
            personKeys = Set.of(StartTask.CLIENT);
        } else {
            Map<String, TaskProperty> nextProperty = properties.get(StartTask.TRIGGERED_BY).getNext();
            personKeys = userService.getUserIdsFromUserIdsAndRolesIds(new HashSet<>((List<String>) nextProperty.get(personType).getValue()));
        }

        personKeys.forEach(personKey -> {
            TriggerKey triggerKey = new TriggerKey();
            triggerKey.setPersonKey(personKey);
            triggerKey.setFormKey(formKey);
            triggerKey.setWorkflowDefinition(workflowDefinition);
            triggerKeys.add(triggerKey);
        });

        workflowDefinition.setTriggerKeys(triggerKeys);
    }
}
