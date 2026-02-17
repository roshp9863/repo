package com.inn.multithreading.scaler;

public class Child implements Runnable {
    @Override
    public void run() {
        System.out.println("Hi Child class run method : " + Thread.currentThread().getName());
    }

}
