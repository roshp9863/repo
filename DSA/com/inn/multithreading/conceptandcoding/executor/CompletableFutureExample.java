package com.inn.multithreading.conceptandcoding.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CompletableFutureExample {

    public static void main(String[] args) {
        
        try{
            ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1, 1, TimeUnit.SECONDS, 
            new ArrayBlockingQueue<>(2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

            CompletableFuture<Integer> futureObj = CompletableFuture.supplyAsync(() -> {
                    return 1;
            }, executor);

            System.out.println("1st time Task 1 is done: " + futureObj.get());
        }
        catch(Exception e){
            System.out.println("Exception: " + e.getMessage());
        }    


        CompletableFuture<Integer> futureObj2 = CompletableFuture.supplyAsync(() -> {
            return 1;
        });


        //thenApply is a synchronous method that is used to transform the result of a CompletableFuture.
//main thread is working
        CompletableFuture<String> futureObj3 = CompletableFuture.supplyAsync(() -> { // new thread is created (thread 1)
            System.out.println("Task 1 is executing : "+ Thread.currentThread().getName());
            return "Concept and";
        }).thenApply((String val) -> {
            System.out.println("Task 1 is executing then apply : "+ Thread.currentThread().getName());
            return val + "Coding"; // same thread 1 is working on this also
        });


        CompletableFuture<String> futureObj4 = CompletableFuture.supplyAsync(() -> { // new thread is created (thread 1)
            System.out.println("Task 1 is executing : "+ Thread.currentThread().getName());
            return "Concept and";
        }).thenApplyAsync((String val) -> { // it will use different thread from fork-join pool if executor is not given
            System.out.println("Task 1 is executing then apply : "+ Thread.currentThread().getName());
            return val + "Coding"; // same thread 1 is working on this also
        });

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1, 1, TimeUnit.SECONDS, 
            new ArrayBlockingQueue<>(2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());


        CompletableFuture<String> futureObj5 = CompletableFuture.supplyAsync(() -> { // new thread is created (thread 1)
            System.out.println("Task 1 is executing : "+ Thread.currentThread().getName());
            return "Concept and";
        }).thenApplyAsync((String val) -> { // it uses different thread from executor because executor is given
            System.out.println("Task 1 is executing then apply : "+ Thread.currentThread().getName());
            return val + "Coding"; // same thread 1 is working on this also
        }, executor); // using executor to run the task in a thread pool

    }

}
