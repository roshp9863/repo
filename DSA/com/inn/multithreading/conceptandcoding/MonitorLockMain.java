package com.inn.multithreading.conceptandcoding;


//What is Monitor Lock?
// It helps to make sure that only 1 thread goes inside the particular section of code
//(a synchronized block or method)

class MonitorLock {
	public synchronized void task1() {
		try {
			System.out.println("inside task1 : "+ Thread.currentThread().getName());
			Thread.sleep(10000);
			System.out.println("inside task1 after sleep : "+ Thread.currentThread().getName());
		}catch(Exception e) {
			
		}
	}
	

	public void task2() {
			System.out.println("inside task2 before synchronized: "+ Thread.currentThread().getName());
			synchronized (this) {
				System.out.println("inside task2 after synchronized: "+ Thread.currentThread().getName());
				
			}
	}

	public void task3() {
		System.out.println("inside task3 : "+ Thread.currentThread().getName());
}


}

public class MonitorLockMain {
	public static void main(String[] args) {
		MonitorLock obj = new MonitorLock();
		
		Thread t = new Thread(()->obj.task1());
		Thread t1 = new Thread(()->obj.task1());
		Thread t2 = new Thread(()->obj.task1());
		
//		t1.start();
//		t2.start();
//		t3.start();

		MonitorLock obj1 = new MonitorLock();
		
		Thread t3 = new Thread(()->obj1.task1());
		Thread t4 = new Thread(()->obj1.task2());
		Thread t5 = new Thread(()->obj1.task3());

		
		t3.start(); 
		// in obj1 t3 acquire monitor lock first.. 
		//when it gets completed then only it will relaese the lock for task 2 
		//or for t4 thread to acquire lock for its synchronized block
		t4.start();
		t5.start();

		
	}
}
