// package com.inn.multithreading.scaler;

// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;

// public class EvenOddUsingThread {
//     public static void main(String[] args) {
//         ExecutorService executorService = Executors.newFixedThreadPool(2);

//         for(int i=0; i<=10; i++) {
//             int number = i;
//             if(number%2==0) {
//                 executorService.execute(new Even(number));
//                 wait(1000);
//             } else {
//                 executorService.execute(new Odd(number));    
//             }
//         }

//         executorService.shutdown();
//     }
// }


// class Even implements Runnable {

//     int number;
//     public Even(int number) {
//         this.number = number;
//     }

//     @Override
//     public void run() {
//         System.out.println("Even: " + number + " " + Thread.currentThread().getName());
//     }
// }

// class Odd implements Runnable {

//     int number;
//     public Odd(int number) {
//         this.number = number;
//     }


//     @Override
//     public void run() {
//         System.out.println("ODD: " + number + " " + Thread.currentThread().getName());
//     }
// }