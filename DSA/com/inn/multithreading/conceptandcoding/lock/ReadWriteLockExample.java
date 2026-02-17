package com.inn.multithreading.conceptandcoding.lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample {
    
    //ReadWriteLock is used when we have multiple threads reading and writing to the same resource.
    //It allows multiple threads to read the resource at the same time, but only one thread can write to it at a time.
    //This improves performance when there are more reads than writes.
    
    public static void main(String[] args) {
        ReadWriteLock lock = new ReentrantReadWriteLock();

        SharedResourceReadWrite r1 = new SharedResourceReadWrite();
        Thread t1 = new Thread(() -> {
            r1.produce(lock);
        });
        Thread t2 = new Thread(() -> {
            r1.produce(lock);
        });
        SharedResourceReadWrite r2 = new SharedResourceReadWrite();
        Thread t3 = new Thread(() -> {
            r2.consume(lock);
        });
        t1.start();
        t2.start();
        t3.start();
    }
    
}

class SharedResourceReadWrite {
    
    boolean isAvailable = false;
    
    public void produce(ReadWriteLock lock) {

        try {
            lock.readLock().lock();
            long now = System.currentTimeMillis();
            
            System.out.println( now + " : Trying to acquire lock for producing resource "+ Thread.currentThread().getName());
            Thread.sleep(6000);
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
            long now = System.currentTimeMillis();
            System.out.println( now + " : Lock released for producing resource "+ Thread.currentThread().getName());
            
        }
    }
    
    public void consume(ReadWriteLock lock) {
        try {
            lock.writeLock().lock();
            long now = System.currentTimeMillis();
            System.out.println( now + " : Trying to acquire lock for Consuming resource "+ Thread.currentThread().getName());
            Thread.sleep(6000);
        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
            long now = System.currentTimeMillis();
            System.out.println( now + " : Lock released for consuming resource "+ Thread.currentThread().getName());
        }
    }
}