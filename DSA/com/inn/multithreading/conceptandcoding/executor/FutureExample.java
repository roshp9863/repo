package com.inn.multithreading.conceptandcoding.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FutureExample {

    public static void main(String[] args) {
        // Future is an interface that represents the result of an asynchronous computation.
        // It allows you to check if the computation is complete, wait for it to complete, and retrieve the result.
        // It is used in conjunction with ExecutorService to submit tasks and retrieve their results.
        
        // FutureTask is a concrete implementation of Future that can be used to wrap a Callable or Runnable task.
        // It provides additional functionality such as canceling the task and checking if it was interrupted.
        // FutureTask is a blocking queue that can be used to store the result of a task.
        // It is used to wrap a Callable or Runnable task and can be used to cancel the task and check if it was interrupted.


        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1, 1, TimeUnit.SECONDS, 
            new ArrayBlockingQueue<>(2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
            
        executor.allowCoreThreadTimeOut(true);

        Future<?> futureObj =  executor.submit( () -> {
            try {
                Thread.sleep(7000);
                System.out.println("Task 1 is executing");

            } catch (InterruptedException e) {
            }
        });

        System.out.println("1st time Task 1 is done: " + futureObj.isDone());

        try{
            futureObj.get(2, TimeUnit.SECONDS);
        }catch(Exception e) {
            System.out.println("TimeOut exception");
        }
        
        try{
            futureObj.get();
        }catch(Exception e) {
        }    
        System.out.println("Task 1 is done: " + futureObj.isDone());
        System.out.println("Task 1 is cancelled: " + futureObj.isCancelled());




    }
}
