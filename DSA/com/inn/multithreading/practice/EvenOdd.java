package com.inn.multithreading.practice;

public class EvenOdd {
    public static void main(String[] args) {
        PrintEvenOdd printer = new PrintEvenOdd();

        Thread evenThread = new Thread(() -> printer.printEven(), "EvenThread");
        Thread oddThread = new Thread(() -> printer.printOdd(), "OddThread");

        evenThread.start();
        oddThread.start();
    }
}

class PrintEvenOdd {
    private int number = 1;
    private final int MAX = 100;

    public synchronized void printOdd() {
        while (number <= MAX) {
            if (number % 2 == 1) {
                System.out.println(Thread.currentThread().getName() + " : " + number);
                number++;
                notify();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public synchronized void printEven() {
        while (number <= MAX) {
            if (number % 2 == 0) {
                System.out.println(Thread.currentThread().getName() + " : " + number);
                number++;
                notify();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}