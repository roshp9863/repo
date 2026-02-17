package com.inn.multithreading.chatgpt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvenOddPlay {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable evenTask = () -> {
            for (int i = 0; i <= 10; i += 2) {
                System.out.println("Even: " + i + " Thread: " + Thread.currentThread().getName());
            }
        };
        Runnable oddTask = () -> {
            for (int i = 1; i <= 10; i += 2) {
                System.out.println("Odd: " + i + " Thread: " + Thread.currentThread().getName());
            }
        };
        executor.execute(evenTask);
        executor.execute(oddTask);

    }

}


class EvenOddLockPlay {

    private static final Object lock = new Object();
    private static int number = 1;
    private static final int MAX = 10;

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable evenTask = () -> {
            while (number <= MAX) {
                synchronized (lock) {
                    while (number % 2 != 0) {  // Not even turn
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    if(number <= MAX) {
                        System.out.println("Even: " + number +
                                " Thread: " + Thread.currentThread().getName());
                        number++;
                        lock.notifyAll();
                    }
                }
            }
        };

        Runnable oddTask = () -> {
            while (number <= MAX) {
                synchronized (lock) {
                    while (number % 2 == 0) { // Not odd turn
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    if(number <= MAX) {
                        System.out.println("Odd : " + number +
                                " Thread: " + Thread.currentThread().getName());
                        number++;
                        lock.notifyAll();
                    }
                }
            }
        };

        executor.execute(oddTask);
        executor.execute(evenTask);

        executor.shutdown();
    }
}
