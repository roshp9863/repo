package com.inn.multithreading.chatgpt;

import java.util.*;
import java.util.concurrent.*;

public class Level2 {
    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            int taskId = i;

            Future<Integer> future = executor.submit(() -> {
                return taskId * 2;
            });

            futures.add(future);
        }

        List<Integer> results = new ArrayList<>();
        for (Future<Integer> future : futures) {
            results.add(future.get());
        }

        executor.shutdown();

        Collections.sort(results);
        System.out.println("Results: " + results);
    }
}
