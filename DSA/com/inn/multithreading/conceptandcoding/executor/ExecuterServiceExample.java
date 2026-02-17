package com.inn.multithreading.conceptandcoding.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecuterServiceExample {

    public static void main(String[] args) {
        
        ExecutorService e = Executors.newFixedThreadPool(5);

        Future<?> f = e.submit(() -> {
            System.out.println("Task 1 is executing");
        });
        
    }

}
