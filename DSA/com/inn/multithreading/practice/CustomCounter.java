package com.inn.multithreading.practice;

public class CustomCounter {
    public static void main(String[] args) throws InterruptedException {
        // CustomCounterClass c = new CustomCounterClass();

        // Thread t1 = new Thread(() -> c.countermethod());
        // Thread t2 = new Thread(() -> c.countermethod());
        // Thread t3 = new Thread(() -> c.countermethod());
        // Thread t4 = new Thread(() -> c.countermethod());
        // Thread t5 = new Thread(() -> c.countermethod());
        // Thread t6 = new Thread(() -> c.countermethod());
        // Thread t7 = new Thread(() -> c.countermethod());
        // Thread t8 = new Thread(() -> c.countermethod());
        // Thread t9 = new Thread(() -> c.countermethod());
        // Thread t10 = new Thread(() -> c.countermethod());
        // t1.start();
        // t2.start();
        // t3.start();
        // t4.start();
        // t5.start();
        // t6.start();
        // t7.start();
        // t8.start();
        // t9.start();
        // t10.start();

        CustomCounterClass c = new CustomCounterClass();

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> c.countermethod());
            threads[i].start();
        }

        // Wait for all threads to finish

        for (Thread t : threads) {
            t.join();
        }


        c.getValue();
    }
}

class CustomCounterClass {

    int count = 0;

    public synchronized void countermethod() {
        for(int i=0; i<1000; i++)
            count++;
    }

    public void getValue() {
        System.out.println("COUNT : "+count);
    }

}
