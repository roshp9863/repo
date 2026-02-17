package com.inn.multithreading.chatgpt;

import java.util.concurrent.*;

public class Level5 {
    public static void main(String[] args) throws Exception {

        CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(() -> {
                    System.out.println("Working on: " + Thread.currentThread().getName());
                    return 10 * 2;
                });

        System.out.println("Main thread continues...");

        // Block only when result is required
        Integer result = future.get();

        System.out.println("Result = " + result);


        //LEVEL-5.1: Chain another async step → .thenApply()
        CompletableFuture<Integer> future2 =
        CompletableFuture.supplyAsync(() -> 10)
                .thenApply(num -> num * 10)
                .thenApply(num -> num + 5);

        System.out.println("Final Result = " + future2.get());


        //LEVEL-5.2: Run 2 tasks in parallel → combine using .thenCombine()
        CompletableFuture<Integer> future1 =
        CompletableFuture.supplyAsync(() -> 10);

        CompletableFuture<Integer> future3 =
                CompletableFuture.supplyAsync(() -> 20);

        CompletableFuture<Integer> combined =
                future1.thenCombine(future3, (a, b) -> a + b);

        System.out.println("Combined result = " + combined.get());


    }
}
