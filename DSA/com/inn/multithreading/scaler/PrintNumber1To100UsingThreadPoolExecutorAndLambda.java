package com.inn.multithreading.scaler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrintNumber1To100UsingThreadPoolExecutorAndLambda {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            int number = i; // effectively final for lambda
            executorService.execute(() -> {
                System.out.println("Number: " + number + " " + Thread.currentThread().getName());
            });
        }  
        executorService.shutdown();

    }

}
