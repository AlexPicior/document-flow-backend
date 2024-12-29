package com.licenta.licenta.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Engine {
    private ExecutorService executorService;
    public void start() {
        executorService = Executors.newVirtualThreadPerTaskExecutor();
        run();
    }
    public void run() {
        executorService.submit(() -> {
            System.out.println("Hello, World!");
        });
    }
}
