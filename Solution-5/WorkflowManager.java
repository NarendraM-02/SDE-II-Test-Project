package com.example.workflow;

import java.util.concurrent.*;

public class WorkflowManager {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public void runWorkflow() {
        Callable<String> taskA = () -> { doWork("A"); return "A done"; };
        Callable<String> taskB = () -> { doWork("B"); return "B done"; };
        Callable<String> taskC = () -> { doWork("C"); return "C done"; };
        Callable<String> taskD = () -> { doWork("D"); return "D done"; };
        Callable<String> taskE = () -> { doWork("E"); return "E done"; };
        Callable<String> taskF = () -> { doWork("F"); return "F done"; };

        CompletableFuture<Void> chain1 = CompletableFuture
            .supplyAsync(() -> { await(taskA); return null; }, executor)
            .thenRunAsync(() -> await(taskB), executor)
            .thenRunAsync(() -> await(taskC), executor)
            .exceptionally(ex -> { logError("Chain1", ex); return null; });

        CompletableFuture<Void> chain2 = CompletableFuture
            .supplyAsync(() -> { await(taskD); return null; }, executor)
            .thenRunAsync(() -> await(taskE), executor)
            .thenRunAsync(() -> await(taskF), executor)
            .exceptionally(ex -> { logError("Chain2", ex); return null; });

        CompletableFuture.allOf(chain1, chain2).join();
        executor.shutdown();
    }

    private <T> T await(Callable<T> task) {
        try {
            return task.call();
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    private void doWork(String name) throws InterruptedException {
        System.out.println("Starting " + name);
        Thread.sleep(500);
        if (Math.random() < 0.1) {
            throw new RuntimeException("Task " + name + " failed");
        }
        System.out.println("Completed " + name);
    }

    private void logError(String chain, Throwable ex) {
        System.err.println("Error in " + chain + ": " + ex.getCause().getMessage());
    }

    public static void main(String[] args) {
        new WorkflowManager().runWorkflow();
    }
}
