package com.licenta.licenta.engine.workflow.dto.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConditionDTO {
    private String formVariableName;
    private String leftOperand;
    private String operation;
    private String rightOperand;
}
