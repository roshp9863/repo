package com.inn.functionInterface.practice;

//class ThreadDemo implements Runnable { // example 1
//
//	@Override
//	public void run() {
//		System.out.println("RUN..... using implementation");
//		
//	}
//	
//}

public class RunnableInterfaceExample {
	
	public static void main(String[] args) {
//		Thread thread = new Thread(new ThreadDemo()); // example 1
//		thread.start();
		
//		Runnable run = () -> { // example 2
//			System.out.println("RUN.... using lambda");
//		};
//		
//		Thread threadlambda = new Thread(run); // example 2
//		threadlambda.start();

//		Runnable run1 = () -> System.out.println("RUN.... using lambda"); // example 3
//		
//		Thread threadlambda1 = new Thread(run1); // example 3
//		threadlambda1.start();

		
		Thread threadlambda2 = new Thread(() -> System.out.println("RUN.... using lambda"));
		threadlambda2.start();
	}

}
