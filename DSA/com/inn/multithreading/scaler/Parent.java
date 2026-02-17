package com.inn.multithreading.scaler;

public class Parent {
    public static void main(String[] args) {
        System.out.println("Hi 1 : " + Thread.currentThread().getName());

        Child child = new Child();
        Thread thread = new Thread(child);
        thread.start();
        System.out.println("Hi Parent class main method : " + Thread.currentThread().getName());

    }
}
