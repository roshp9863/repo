package com.inn.multithreading.conceptandcoding.lock;

import java.util.concurrent.locks.StampedLock;

public class StampedOptimisticLockExample {

    public static void main(String[] args) {
        SharedResourceOptimistic r1 = new SharedResourceOptimistic();

        StampedLock lock = new StampedLock();

        Thread t1 = new Thread(() -> {
            r1.produce(lock);
        });

        Thread t2 = new Thread(() -> {
            r1.produce(lock);
        });
        Thread t3 = new Thread(() -> {
            r1.consume(lock);
        });

        t1.start();
//        t2.start();
        t3.start();
    }

}

class SharedResourceOptimistic {
    
    boolean isAvailable = false;
    int a = 10;
    public void produce(StampedLock lock) {
        long stamp = lock.tryOptimisticRead();

        try {
            long now = System.currentTimeMillis();
            a=11;
            System.out.println( now + " : Trying to acquire lock for producing resource "+ Thread.currentThread().getName() + " : " + a);
            Thread.sleep(6000);
            if(lock.validate(stamp)) {
                System.out.println("Optimistic lock is valid : " + a);
            } else {
                a=10;
                System.out.println("Optimistic lock is not valid");
            }

        }catch(Exception e) {
            e.printStackTrace();
        } 
    }
    
    public void consume(StampedLock lock) {
        long stamp = lock.writeLock();

        try {
            long now = System.currentTimeMillis();
            a=9;
            System.out.println( now + " : Trying to acquire lock for Consuming resource "+ Thread.currentThread().getName() + " : " + a);
            Thread.sleep(6000);
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamp);
            long now = System.currentTimeMillis();
            System.out.println( now + " : Lock released for consuming resource "+ Thread.currentThread().getName() + " : " + a);
        }
    }
}