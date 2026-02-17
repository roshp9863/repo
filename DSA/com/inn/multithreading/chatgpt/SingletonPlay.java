package com.inn.multithreading.chatgpt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {  // First Check (no lock)
            synchronized (Singleton.class) {
                if (instance == null) {  // Second Check (with lock)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

class Singleton1 {
    private static  Singleton1 instance;

    private Singleton1() {}

    public synchronized Singleton1 getInstance() {
            
                if (instance == null) {  // Second Check (with lock)
                    instance = new Singleton1();
                }
            
        return instance;
    }
}


class A implements Runnable {
    @Override
    public void run() {
        Singleton s = Singleton.getInstance();
        System.out.println("A using: " + s + " Thread: " + Thread.currentThread().getName());
    }
}

class B implements Runnable {
    @Override
    public void run() {
        Singleton s = Singleton.getInstance();
        System.out.println("B using: " + s + " Thread: " + Thread.currentThread().getName());
    }
}    

public class SingletonPlay {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();

        ExecutorService executor = Executors.newFixedThreadPool(20);
        executor.execute(a);
        executor.execute(b);
        executor.execute(a);
        executor.execute(b);
        executor.execute(a);
        executor.execute(b);
        executor.execute(a);
        executor.execute(b);
        executor.execute(a);
        executor.execute(b);
        executor.execute(a);
        executor.execute(b);
        executor.execute(a);
        executor.execute(b);
        executor.execute(a);
        executor.execute(b);
        executor.execute(a);
        executor.execute(b);
        executor.execute(a);
        executor.execute(b);
        executor.shutdown();
        
    }
}


