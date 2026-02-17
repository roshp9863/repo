package com.inn.multithreading.scaler.producerconsumer;

public class Consumer implements Runnable{

    Store store;
    public Consumer(Store store) {
        this.store = store;
    }

    @Override
    public void run() {
        store.consume();
    }   

}
