package com.licenta.licenta.engine;

import com.licenta.licenta.engine.workflow.Workflow;
import com.licenta.licenta.engine.workflow.creation.WorkflowInstanceCreator;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class Engine {
    private ExecutorService executorService;
    private final WorkflowInstanceCreator workflowInstanceCreator;

    public void start() {
        executorService = Executors.newVirtualThreadPerTaskExecutor();
    }
    public void run() {
        executorService.submit(() -> {
            Workflow workflow = workflowInstanceCreator.createWorkflow();

            workflow.start(new HashMap<>());
        });
    }
}
