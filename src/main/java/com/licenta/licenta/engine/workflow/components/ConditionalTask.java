package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.business.form.dto.FormFieldRecordDTO;
import com.licenta.licenta.business.form.dto.FormRecordDTO;
import com.licenta.licenta.business.form.model.FormFieldRecord;
import com.licenta.licenta.business.form.model.FormRecord;
import com.licenta.licenta.engine.workflow.AbstractWorkflowComponent;
import com.licenta.licenta.engine.workflow.WorkflowComponent;
import com.licenta.licenta.engine.workflow.dto.task.ConditionDTO;
import com.licenta.licenta.engine.workflow.dto.task.TaskProperty;
import com.licenta.licenta.engine.workflow.type.ConditionType;
import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class ConditionalTask extends AbstractWorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.CONDITIONAL_TASK;

    public static final String CONDITION = "condition";

    private ConditionDTO condition;

    public static final String FORM_VARIABLE_NAME = "formVariableName";
    public static final String LEFT_OPERAND = "leftOperand";
    public static final String OPERATION = "operation";
    public static final String RIGHT_OPERAND = "rightOperand";

    // Routes
    public static final String YES = "yes";
    public static final String NO = "no";

    public ConditionalTask(Map<String, TaskProperty> properties) {
        Map<String, String> conditionMap = (Map<String, String>) properties.get(CONDITION).getValue();
        ConditionDTO newCondition = new ConditionDTO();
        newCondition.setFormVariableName(conditionMap.get(FORM_VARIABLE_NAME));
        newCondition.setLeftOperand(conditionMap.get(LEFT_OPERAND));
        newCondition.setOperation(conditionMap.get(OPERATION));
        newCondition.setRightOperand(conditionMap.get(RIGHT_OPERAND));
        this.condition = newCondition;
    }

    private WorkflowComponent nextTaskForYes;
    private WorkflowComponent nextTaskForNo;

    @Override
    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        FormFieldRecordDTO leftOperandFieldRecord = getQueriedFormFieldRecord(inputParameters);
        if (isConditionSatisfied(leftOperandFieldRecord.getValue(), leftOperandFieldRecord.getArrayValues())) {
            setNext(nextTaskForYes);
        } else {
            setNext(nextTaskForNo);
        }

//        System.out.println("Properties of " + TYPE + ": " + FORM_ID + ": " + condition.getFormId() + ", "
//                + LEFT_OPERAND + ": " + condition.getLeftOperand() + ", " + OPERATION + ": " + condition.getOperation()
//                + ", " + RIGHT_OPERAND + ": " + condition.getRightOperand());
        return inputParameters;
    }

    private FormFieldRecordDTO getQueriedFormFieldRecord(Map<String, Object> inputParameters) {
        FormRecordDTO queriedForm = (FormRecordDTO)inputParameters.get(condition.getFormVariableName());
        //get the queried field
        return queriedForm.getFieldRecords().stream()
                .filter(formFieldRecord -> formFieldRecord.getFormField().getId().toString().equals(condition.getLeftOperand()))
                .findFirst().orElseThrow(() -> new RuntimeException("Left operand not found")); // TODO put better exception
    }

    private boolean isConditionSatisfied(String leftOperandValue, List<String> leftOperandArrayValues) {
        switch (ConditionType.fromString(condition.getOperation()) ) {
            case IS -> {
                return leftOperandValue.equals(condition.getRightOperand());
            }
            case IS_NOT -> {
                return !leftOperandValue.equals(condition.getRightOperand());
            }
            case CONTAINS -> {
                return leftOperandValue != null && !leftOperandValue.isEmpty()
                        ? leftOperandValue.contains(condition.getRightOperand())
                        : leftOperandArrayValues.contains(condition.getRightOperand());
            }
            case NOT_CONTAINS -> {
                return leftOperandValue != null && !leftOperandValue.isEmpty()
                        ? !leftOperandValue.contains(condition.getRightOperand())
                        : !leftOperandArrayValues.contains(condition.getRightOperand());
            }
            case EQUALS -> {
                return Double.parseDouble(leftOperandValue) == Double.parseDouble(condition.getRightOperand());
            }
            case NOT_EQUALS -> {
                return Double.parseDouble(leftOperandValue) != Double.parseDouble(condition.getRightOperand());
            }
            case GREATER_THAN -> {
                return Double.parseDouble(leftOperandValue) > Double.parseDouble(condition.getRightOperand());
            }
            case GREATER_THAN_EQUALS -> {
                return Double.parseDouble(leftOperandValue) >= Double.parseDouble(condition.getRightOperand());
            }
            case LESS_THAN -> {
                return Double.parseDouble(leftOperandValue) < Double.parseDouble(condition.getRightOperand());
            }
            case LESS_THAN_EQUALS -> {
                return Double.parseDouble(leftOperandValue) <= Double.parseDouble(condition.getRightOperand());
            }
        }
        return false;
    }
}
