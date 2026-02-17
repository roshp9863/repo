package com.inn.multithreading.scaler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrintNumber1To100UsingThreadPoolExecutor {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            NumberPrint numberPrint = new NumberPrint(i);
            executorService.execute(numberPrint);
        }  
        executorService.shutdown();

    }
}
