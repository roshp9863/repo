package com.inn.multithreading.scaler;

public class NumberPrint implements Runnable {
    private int number;

    public NumberPrint(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        System.out.println("Number: " + number + " printed by " + Thread.currentThread().getName());
    }

}
