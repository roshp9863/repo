package com.inn.multithreading.chatgpt;

import java.util.concurrent.*;

public class Level4 {
    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        // Producer
        Runnable producer = () -> {
            int i = 1;
            try {
                while (i <= 10) {
                    System.out.println("Producing: " + i + " thread: " + Thread.currentThread().getName());
                    queue.put(i);  // waits if queue is full
                    i++;
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Consumer
        Runnable consumer = () -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    int item = queue.take();  // waits if queue is empty
                    System.out.println("Consuming: " + item + " thread: " + Thread.currentThread().getName());
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(producer);
        executor.submit(producer);
        executor.submit(consumer);
        executor.submit(consumer);
        executor.submit(consumer);

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Producer-Consumer flow completed!");
    }
}
