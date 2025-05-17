package com.licenta.licenta.engine.workflow.mapper;

import com.licenta.licenta.business.form.dto.FormDTO;
import com.licenta.licenta.business.form.service.FormService;
import com.licenta.licenta.business.role.dto.RoleDTO;
import com.licenta.licenta.business.role.service.RoleService;
import com.licenta.licenta.engine.workflow.dto.WorkflowComponentDTO;
import com.licenta.licenta.engine.workflow.dto.WorkflowComponentPropertyDTO;
import com.licenta.licenta.engine.workflow.dto.WorkflowComponentPropertyOptionDTO;
import com.licenta.licenta.engine.workflow.model.WorkflowComponentModel;
import com.licenta.licenta.engine.workflow.model.WorkflowComponentProperty;
import com.licenta.licenta.engine.workflow.model.WorkflowComponentPropertyConstantOption;
import com.licenta.licenta.engine.workflow.type.InitialOptionsType;
import com.licenta.licenta.engine.workflow.type.TaskPropertyType;
import com.licenta.licenta.engine.workflow.type.VariableType;
import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import com.licenta.licenta.security.dto.UserDTO;
import com.licenta.licenta.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WorkflowComponentMapper {
    private final FormService formService;
    private final UserService userService;
    private final RoleService roleService;

    public List<WorkflowComponentModel> allToEntity(List<WorkflowComponentDTO> workflowComponentDTOList) {
        return workflowComponentDTOList.stream().map(this::toEntity).toList();
    }

    public List<WorkflowComponentDTO> allToDTO(List<WorkflowComponentModel> workflowComponentList) {
        return workflowComponentList.stream().map(this::toDTO).toList();
    }

    public WorkflowComponentModel toEntity(WorkflowComponentDTO workflowComponentDTO) {
        WorkflowComponentModel workflowComponentModel = new WorkflowComponentModel();
        workflowComponentModel.setLabel(workflowComponentDTO.getLabel());
        workflowComponentModel.setDescription(workflowComponentDTO.getDescription());
        workflowComponentModel.setType(WorkflowComponentType.fromString(workflowComponentDTO.getType()));
        workflowComponentModel.setProperties(getMappedModelProperties(workflowComponentDTO.getProperties(), workflowComponentModel));
        return workflowComponentModel;
    }

    public WorkflowComponentDTO toDTO(WorkflowComponentModel workflowComponentModel) {
        WorkflowComponentDTO workflowComponentDTO = new WorkflowComponentDTO();
        workflowComponentDTO.setLabel(workflowComponentModel.getLabel());
        workflowComponentDTO.setDescription(workflowComponentModel.getDescription());
        workflowComponentDTO.setType(workflowComponentModel.getType().getLabel());
        workflowComponentDTO.setProperties(getMappedDTOProperties(workflowComponentModel.getProperties()));
        return workflowComponentDTO;
    }

    private Map<String, WorkflowComponentPropertyDTO> getMappedDTOProperties(
            List<WorkflowComponentProperty> workflowComponentPropertyList) {
        return workflowComponentPropertyList.stream()
                .collect(Collectors.toMap(WorkflowComponentProperty::getPropertyKey, this::getMappedDTOProperty));
    }

    private WorkflowComponentPropertyDTO getMappedDTOProperty(WorkflowComponentProperty workflowComponentProperty) {
        WorkflowComponentPropertyDTO workflowComponentPropertyDTO = new WorkflowComponentPropertyDTO();
        workflowComponentPropertyDTO.setOrderNumber(workflowComponentProperty.getOrderNumber());
        workflowComponentPropertyDTO.setLabel(workflowComponentProperty.getLabel());
        workflowComponentPropertyDTO.setDescription(workflowComponentProperty.getDescription());
        workflowComponentPropertyDTO.setType(workflowComponentProperty.getType().getLabel());
        workflowComponentPropertyDTO.setVariableType(workflowComponentProperty.getVariableType() != null
                ? workflowComponentProperty.getVariableType().getLabel() : null);
        workflowComponentPropertyDTO.setOptionsType(workflowComponentProperty.getOptionsType() != null
                ? workflowComponentProperty.getOptionsType().getLabel() : null);
        workflowComponentPropertyDTO.setInitialOptionTypes(workflowComponentProperty.getInitialOptionsTypes() != null
                ? Arrays.stream(workflowComponentProperty.getInitialOptionsTypes()).map(InitialOptionsType::getLabel).toList() : null);
        workflowComponentPropertyDTO.setOptions(getMappedDTOOptions(workflowComponentProperty));
        setMappedDTONextProperties(workflowComponentProperty.getNextProperties(),
                workflowComponentProperty.getPropertyKey(), workflowComponentPropertyDTO);

        return workflowComponentPropertyDTO;
    }

    private List<WorkflowComponentPropertyOptionDTO> getMappedDTOOptions(WorkflowComponentProperty workflowComponentProperty) {
        if (workflowComponentProperty.getInitialOptionsTypes() == null) {
            return null;
        }
        List<WorkflowComponentPropertyOptionDTO> workflowComponentPropertyOptionDTOList = new ArrayList<>();
        Arrays.stream(workflowComponentProperty.getInitialOptionsTypes()).forEach(initialOptionsType -> {
            switch (initialOptionsType) {
                case CONSTANT -> workflowComponentPropertyOptionDTOList.addAll(workflowComponentProperty.getConstantOptions()
                        .stream().map(workflowComponentPropertyConstantOption -> new WorkflowComponentPropertyOptionDTO(
                                workflowComponentPropertyConstantOption.getLabel(),
                                workflowComponentPropertyConstantOption.getValue())).toList());
                case FORMS -> workflowComponentPropertyOptionDTOList.addAll(getFormList());
                case ROLES -> workflowComponentPropertyOptionDTOList.addAll(getRolesList());
                case EMPLOYEES -> workflowComponentPropertyOptionDTOList.addAll(getEmployeesList());
            }
        });
        return workflowComponentPropertyOptionDTOList;
    }

    private List<WorkflowComponentPropertyOptionDTO> getFormList() {
        //TODO replace 1 with the current organisation of the user
        List<FormDTO> formDTOS = formService.getAllByOrganisationId(1L);
        return formDTOS.stream()
                .map(formDTO -> new WorkflowComponentPropertyOptionDTO(formDTO.getLabel(), formDTO.getId().toString())).toList();
    }

    private List<WorkflowComponentPropertyOptionDTO> getRolesList() {
        //TODO replace 1 with the current organisation of the user
        List<RoleDTO> roleDTOs = roleService.getAllRolesByOrganisationId(1L);
        return roleDTOs.stream().map(roleDTO -> new WorkflowComponentPropertyOptionDTO(
                roleDTO.getLabel(), roleDTO.getId().toString() + "r")).collect(Collectors.toList());
    }

    private List<WorkflowComponentPropertyOptionDTO> getEmployeesList() {
        //TODO replace 1 with the current organisation of the user
        List<UserDTO> userDTOs = userService.getAllUsersByOrganisationId(1L);
        return userDTOs.stream().map(userDTO -> new WorkflowComponentPropertyOptionDTO(
                userDTO.getName(), userDTO.getId().toString() + "e")).collect(Collectors.toList());
    }

    private void setMappedDTONextProperties(List<WorkflowComponentProperty> nextProperties, String parentKey,
                                            WorkflowComponentPropertyDTO workflowComponentPropertyDTO) {
        if (nextProperties == null || nextProperties.isEmpty()) {
            return;
        }
        Map<String, WorkflowComponentPropertyDTO> nextPropertiesDTOMap = nextProperties.stream()
                .collect(Collectors.toMap(WorkflowComponentProperty::getPropertyKey, nextProperty -> {
                    WorkflowComponentPropertyDTO nextPropertyDTO = getMappedDTOProperty(nextProperty);
                    nextPropertyDTO.setParentKey(parentKey);
                    return nextPropertyDTO;
                }));
        workflowComponentPropertyDTO.setNext(nextPropertiesDTOMap);
    }

    private List<WorkflowComponentProperty> getMappedModelProperties(Map<String, WorkflowComponentPropertyDTO> propertyDTOMap,
                                                                     WorkflowComponentModel workflowComponentModel) {
        List<WorkflowComponentProperty> workflowComponentPropertyList = new ArrayList<>();
        propertyDTOMap.forEach((key, propertyDTO) -> {
            WorkflowComponentProperty workflowComponentProperty = getMappedModelProperty(propertyDTO, key);
            workflowComponentProperty.setWorkflowComponent(workflowComponentModel);
            workflowComponentPropertyList.add(workflowComponentProperty);
        });
        return workflowComponentPropertyList;
    }

    private WorkflowComponentProperty getMappedModelProperty(WorkflowComponentPropertyDTO propertyDTO, String propertyKey) {
        WorkflowComponentProperty workflowComponentProperty = new WorkflowComponentProperty();

        workflowComponentProperty.setOrderNumber(propertyDTO.getOrderNumber());
        workflowComponentProperty.setPropertyKey(propertyKey);
        workflowComponentProperty.setLabel(propertyDTO.getLabel());
        workflowComponentProperty.setDescription(propertyDTO.getDescription());
        workflowComponentProperty.setType(TaskPropertyType.fromString(propertyDTO.getType()));
        workflowComponentProperty.setVariableType(propertyDTO.getVariableType() != null
                ? VariableType.fromString(propertyDTO.getVariableType()) : null);
        workflowComponentProperty.setConstantOptions(getMappedModelConstantOptions(propertyDTO.getOptions(), workflowComponentProperty));
        workflowComponentProperty.setOptionsType(propertyDTO.getOptionsType() != null
                ? VariableType.fromString(propertyDTO.getOptionsType()) : null);
        workflowComponentProperty.setInitialOptionsTypes(getMappedModelInitialOptionsType(propertyDTO.getInitialOptionTypes()));
        setMappedModelNextProperties(workflowComponentProperty, propertyDTO.getNext());

        return workflowComponentProperty;
    }

    private List<WorkflowComponentPropertyConstantOption> getMappedModelConstantOptions(
            List<WorkflowComponentPropertyOptionDTO> optionDTOList, WorkflowComponentProperty workflowComponentProperty) {
        if (optionDTOList == null || optionDTOList.isEmpty()) {
            return null;
        }
        return optionDTOList.stream().map(optionDTO -> {
            WorkflowComponentPropertyConstantOption workflowComponentPropertyConstantOption = new WorkflowComponentPropertyConstantOption();
            workflowComponentPropertyConstantOption.setLabel(optionDTO.getLabel());
            workflowComponentPropertyConstantOption.setValue(optionDTO.getValue());
            workflowComponentPropertyConstantOption.setWorkflowComponentProperty(workflowComponentProperty);
            return workflowComponentPropertyConstantOption;
        }).toList();
    }

    private InitialOptionsType[] getMappedModelInitialOptionsType(List<String> initialOptionsTypes) {
        if (initialOptionsTypes == null || initialOptionsTypes.isEmpty()) {
            return null;
        }
        return initialOptionsTypes.stream().map(InitialOptionsType::fromString).toArray(InitialOptionsType[]::new);
    }

    private void setMappedModelNextProperties(WorkflowComponentProperty workflowComponentProperty,
                                              Map<String, WorkflowComponentPropertyDTO> nextPropertiesMap) {
        if (nextPropertiesMap == null || nextPropertiesMap.isEmpty()) {
            return;
        }
        List<WorkflowComponentProperty> nextProperties = new ArrayList<>();
        nextPropertiesMap.forEach((key, nextPropertyDTO) -> {
            WorkflowComponentProperty nextProperty = getMappedModelProperty(nextPropertyDTO, key);
            nextProperty.setParent(workflowComponentProperty);
            nextProperties.add(nextProperty);
        });
        workflowComponentProperty.setNextProperties(nextProperties);
    }
}
