package com.inn.multithreading.scaler;

public class PrintNumber1To100 {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            NumberPrint numberPrint = new NumberPrint(i);
            Thread thread = new Thread(numberPrint);
            thread.start();
        }
    }
}
