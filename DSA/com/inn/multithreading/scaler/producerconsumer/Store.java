package com.inn.multithreading.scaler.producerconsumer;

import java.util.ArrayList;
import java.util.List;

public class Store {

    private final int CAPACITY = 40;
    private List<Integer> list = new ArrayList<>();

    public synchronized void produce() {
        if(list.size() < CAPACITY) {
            list.add(1);
            System.out.println("Produced: " + list.size());
        } else {
            System.out.println("Store is full, cannot produce");
        }
    }

    public synchronized void consume() {
        if(list.size() > 0) {
            System.out.println("Consumed: " + list.size());
            list.remove(0);
        } else {
            System.out.println("Store is empty, cannot consume");
        }
    }

}
