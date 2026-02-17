package com.inn.multithreading.chatgpt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Counter {
    int count = 0;

    public synchronized void increment() {
        count++;
    }
}

public class Level3 {
    public static void main(String[] args) throws Exception {

        Counter counter = new Counter();
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Final count = " + counter.count);
    }
}
