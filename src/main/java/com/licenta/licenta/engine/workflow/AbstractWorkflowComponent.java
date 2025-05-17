package com.licenta.licenta.engine.workflow;

import com.licenta.licenta.engine.notification.TaskDispatcher;
import com.licenta.licenta.helper.SpringContextHelper;
import com.licenta.licenta.security.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public abstract class AbstractWorkflowComponent implements WorkflowComponent {
    protected WorkflowComponent next;

    @Override
    public void execute(Map<String, Object> inputParameters) {
        Map<String, Object> outputParameters = executeInternal(inputParameters);
        next.execute(outputParameters);
    }

    @Override
    public void setNext(WorkflowComponent next) {
        this.next = next;
    }

    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        return new HashMap<>();
    }

    protected TaskDispatcher getTaskDispatcher() {
        return SpringContextHelper.getBean(TaskDispatcher.class);
    }

    protected UserService getUserService() {
        return SpringContextHelper.getBean(UserService.class);
    }
}
