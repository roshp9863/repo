package com.inn.multithreading;


//Implementing the Runnable interface
class MyRunnable1 implements Runnable {
 @Override
 public void run() {
     // Code that will run in a separate thread
     for (int i = 0; i < 5; i++) {
         System.out.println("Thread running: " + i);
         try {
             Thread.sleep(1000); // Simulate some work
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
     }
 }
}

public class RunnableThread {
 public static void main(String[] args) {
     // Creating an instance of MyRunnable
     MyRunnable1 myRunnable = new MyRunnable1();

     // Creating a thread and passing myRunnable to its constructor
     Thread thread = new Thread(myRunnable);

     // Start the thread
     thread.start();
 }
}
