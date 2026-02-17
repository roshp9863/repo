package com.inn.multithreading.chatgpt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Level1 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 10; i++) {
            int taskId = i;

            executor.submit(() -> {
                System.out.println("Task #" + taskId +
                        " running on " + Thread.currentThread().getName());
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES); // intead of this you can use future also
        System.out.println("All tasks completed!");

        withFuture();


    }

    private static void withFuture() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 11; i <= 20; i++) {
            int taskId = i;

            Future<?> future = executor.submit(() -> {
                System.out.println("Task #" + taskId +
                        " running on " + Thread.currentThread().getName());
            });

            futures.add(future);
        }

        for (Future<?> future : futures) {
            future.get(); 
        }

        executor.shutdown();

        System.out.println("All tasks completed BY FUTURE!");
    }
}
