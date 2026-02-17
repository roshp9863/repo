package com.inn.multithreading.conceptandcoding.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CallableExample {

    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1, 1, TimeUnit.SECONDS, 
            new ArrayBlockingQueue<>(2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
            
        executor.allowCoreThreadTimeOut(true);


            Future<Integer> futureObj2 =  executor.submit( () -> {
            try {
                Thread.sleep(7000);
                System.out.println("Task 2 is executing");
                return 3;

            } catch (Exception e) {
            }
            return 1;
        });

        System.out.println("1st time Task 2 is done: " + futureObj2.isDone());

        try{
            Integer val = futureObj2.get(2, TimeUnit.SECONDS);
            System.out.println("Task 2 is done in limited time: " + val);
        }catch(Exception e) {
            System.out.println("TimeOut exception");
        }
        
        try{
            Integer val = futureObj2.get();
            System.out.println("Task 2 is done: " + val);
        }catch(Exception e) {
        }    
        System.out.println("Task 2 is done: " + futureObj2.isDone());
        System.out.println("Task 2 is cancelled: " + futureObj2.isCancelled());


    }
}
