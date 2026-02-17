package com.inn.multithreading.conceptandcoding;


// Step1: create a runnable object

//- create class that implements runnable interface
//-implement the run() method to tell the task which thread has to do

class MultithreadingLearningRunnable implements Runnable{

	@Override
	public void run() {
		System.out.println("Code executed by thread : "+ Thread.currentThread().getName());
		
	}

}



//Step2: Start the thread

//- create a instance of class that implements runnable
//- pass the runnable object to the thread constructor
//- start the thread

public class MainMultithreadingLearningRunnable {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Going inside the main method: "+ Thread.currentThread().getName());
		MultithreadingLearningRunnable mylearning = new MultithreadingLearningRunnable();
		Thread t1 = new Thread(mylearning);
		t1.start();
		//t1.join(); // if we uncomment this.. first thread execution complete then main method will complete
		System.out.println("Finish main method : "+ t1.currentThread().getName());
	}
}