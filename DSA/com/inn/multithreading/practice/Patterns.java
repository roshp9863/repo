package com.inn.multithreading.practice;

public class Patterns {

    public static void main(String[] args) throws InterruptedException {
        PrintTraingle p = new PrintTraingle();
        for(int i=1; i<=5; i++) {
            final int num = i;
            Thread t = new Thread(() -> p.print(num));
            t.start();
            t.join();  // Wait for this thread to finish before starting the next

        }

    }
}

class PrintTraingle {

    public synchronized void print(int n) {
        for(int i=0; i<n; i++) {
            System.out.print("*");
        }
        System.out.println();
    }



}
