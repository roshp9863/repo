package com.inn.multithreading.scaler.producerconsumer;

import java.util.concurrent.ExecutorService;

public class Client {
    public static void main(String[] args) {
        

        Store store = new Store();

        ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(5);

        int i =0;
        while(i<3) {
            executorService.execute(new Producer(store));
            executorService.execute(new Consumer(store));
            i++;
        }

    }
}
