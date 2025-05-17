package com.licenta.licenta.engine.workflow.service;

import com.licenta.licenta.engine.workflow.dto.WorkflowComponentDTO;
import com.licenta.licenta.engine.workflow.mapper.WorkflowComponentMapper;
import com.licenta.licenta.engine.workflow.model.WorkflowComponentModel;
import com.licenta.licenta.engine.workflow.repository.WorkflowComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowComponentService {
    private final WorkflowComponentRepository workflowComponentRepository;
    private final WorkflowComponentMapper workflowComponentMapper;

    public List<WorkflowComponentDTO> getAllWorkflowComponents() {
        return workflowComponentRepository.findAll().stream().map(workflowComponentMapper::toDTO).toList();
    }

    public WorkflowComponentDTO createWorkflowComponent(WorkflowComponentDTO workflowComponentDTO) {
        WorkflowComponentModel workflowComponent = workflowComponentMapper.toEntity(workflowComponentDTO);
        workflowComponent = workflowComponentRepository.save(workflowComponent);
        return workflowComponentMapper.toDTO(workflowComponent);
    }

    public List<WorkflowComponentDTO> createAllWorkflowComponent(List<WorkflowComponentDTO> workflowComponentDTOList) {
        List<WorkflowComponentModel> workflowComponentModels = workflowComponentRepository
                .saveAll(workflowComponentMapper.allToEntity(workflowComponentDTOList));
        return workflowComponentMapper.allToDTO(workflowComponentModels);
    }

    public void deleteWorkflowComponent(Long workflowComponentId) {
        workflowComponentRepository.deleteById(workflowComponentId);
    }
}
