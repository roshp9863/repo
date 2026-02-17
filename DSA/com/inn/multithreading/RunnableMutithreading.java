package com.inn.multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunnableMutithreading {
	public static void main(String[] args) {
        // Create an ExecutorService with a fixed thread pool size of 1
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        // Submitting a Runnable task using a lambda expression
        executorService.submit(() -> {
            // Code that will run in a separate thread
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread running without implementing Runnable : " + i);
                try {
                    Thread.sleep(1000); // Simulate some work
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Shutdown the ExecutorService to release resources
        executorService.shutdown();
        
        ExecutorService executorService1 = Executors.newFixedThreadPool(1);

        // Submitting a Runnable task to the ExecutorService
        executorService1.submit(new MyRunnable());

        // Shutdown the ExecutorService to release resources
        executorService1.shutdown();
	}     
}


class MyRunnable implements Runnable {
    @Override
    public void run() {
        // Code that will run in a separate thread
        for (int i = 0; i < 5; i++) {
            System.out.println("Thread running w/ implementing runnable: " + i);
            try {
                Thread.sleep(1000); // Simulate some work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}