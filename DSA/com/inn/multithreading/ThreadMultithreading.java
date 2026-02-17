package com.inn.multithreading;

//Extending the Thread class
class MyThread extends Thread {
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

public class ThreadMultithreading {
	public static void main(String[] args) {
		// Creating an instance of MyThread
		MyThread myThread = new MyThread();

		// Start the thread
		myThread.start();
	}
}
