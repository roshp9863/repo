package com.inn.multithreading.practice;

public class CreateThread {

    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        t1.start(); // starts a new thread, calls run() internally


        Thread t2 = new Thread(new MyRunnable());
        t2.start(); // new thread is created and run() is called


        Thread t3 = new Thread(() -> {
            System.out.println("Thread running using Lambda");
        });
        t3.start();


    }

}

class MyThread extends Thread {
    public void run() {
        System.out.println("Thread running using Thread class");
    }

}


class MyRunnable implements Runnable {
    public void run() {
        System.out.println("Thread running using Runnable interface");
    }
}
