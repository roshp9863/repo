package com.inn.multithreading.conceptandcoding.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorExample {

    public static void main(String[] args) {
        
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2, 4, 10, TimeUnit.SECONDS, 
            new ArrayBlockingQueue<>(2), new CustomThreadFactory(), new CustomRejectHandler());
        executor.allowCoreThreadTimeOut(true);
        
        for (int i = 1; i <=7 ; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                }
                System.out.println("Task " + Thread.currentThread().getName() + " is executing");
            });
        }
        executor.shutdown();    

    }
}

class CustomThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(false);
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}


class CustomRejectHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("TASK REJECTED " + r.toString());
        // Handle the rejected task here
    }
}