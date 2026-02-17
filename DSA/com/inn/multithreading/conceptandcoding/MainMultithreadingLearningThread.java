package com.inn.multithreading.conceptandcoding;


//Step 1 : Create a thread class
// - create a thread class that extends thread class
// - Override the run() method to tell the task which thread has to do

class MultithreadingLearningThread  extends Thread{

	@Override
	public void run() {
		System.out.println("Code executed by thread class "+ Thread.currentThread().getName());
	}
}


//Step 2 : Initiate and start the thread
//- create an instance of the subclass
//- call the start method to begin the execution


public class MainMultithreadingLearningThread {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Going inside the main method: "+ Thread.currentThread().getName());

		MultithreadingLearningThread mylearning = new MultithreadingLearningThread();
		mylearning.start();
		mylearning.join();
		System.out.println("Finish main method : "+ mylearning.currentThread().getName());

		
	}
}
