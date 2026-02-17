package com.inn.multithreading.conceptandcoding.lock;

import java.util.concurrent.locks.StampedLock;

public class StampedLockExample {
    
    // StampedLock is a new lock introduced in Java 8. It is a more flexible and scalable alternative to ReentrantLock.
    // It allows for optimistic locking, which can improve performance in certain scenarios.
    // It also provides a way to acquire read and write locks with different levels of granularity.


    public static void main(String[] args) {
        
        SharedResourceStamped r1 = new SharedResourceStamped();

        StampedLock lock = new StampedLock();
        
        Thread t1 = new Thread(() -> {
            r1.produce(lock);
        } );
        
        Thread t2 = new Thread(() -> {
            r1.produce(lock);
        } );
        Thread t3 = new Thread(() -> {
            r1.consume(lock);
        } );
        
        t1.start();
        t2.start();
        t3.start();
        
    }
}

class SharedResourceStamped {
    
    boolean isAvailable = false;
    
    public void produce(StampedLock lock) {
        long stamp = lock.readLock();

        try {
            long now = System.currentTimeMillis();
            
            System.out.println( now + " : Trying to acquire lock for producing resource "+ Thread.currentThread().getName());
            Thread.sleep(6000);
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlockRead(stamp);;
            long now = System.currentTimeMillis();
            System.out.println( now + " : Lock released for producing resource "+ Thread.currentThread().getName());
            
        }
    }
    
    public void consume(StampedLock lock) {
        long stamp = lock.writeLock();

        try {
            long now = System.currentTimeMillis();
            System.out.println( now + " : Trying to acquire lock for Consuming resource "+ Thread.currentThread().getName());
            Thread.sleep(6000);
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamp);
            long now = System.currentTimeMillis();
            System.out.println( now + " : Lock released for consuming resource "+ Thread.currentThread().getName());
        }
    }
}