package com.inn.multithreading.conceptandcoding.lock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
	
	//Locking do not depend on objects like synchronized method.
	
	
	// when obj calling synchronized method, synchronized will put monitor lock on obj.
	
	// Now what if we want critical section on which only one thread can enter irrespective of obj.
	
	// If all threads are using same object than synchronized can be used,
	//but in the scenerio where different
	//objects are using critical section then we need to use Custom locks.
	
	
	
	public static void main(String[] args) {
		ReentrantLock lock = new ReentrantLock();
		
		SharedResourceReentrant r1 = new SharedResourceReentrant();
		
		Thread t1 = new Thread(() -> {
			r1.produce(lock);
		} );

		
		SharedResourceReentrant r2 = new SharedResourceReentrant();
		
		Thread t2 = new Thread(() -> {
			r2.produce(lock);
		} );
		
		t1.start();
		t2.start();

	}

}


class SharedResourceReentrant {

	boolean isAvailable = false;
	public void produce(ReentrantLock lock) {
		
		try {
			lock.lock();
			isAvailable=true;
			long now = System.currentTimeMillis();
			System.out.println(now + ": Lock acquired by " + Thread.currentThread().getName());
			Thread.sleep(6000);
		}catch(Exception e) {
			
		}
		finally {
			lock.unlock();
			long now = System.currentTimeMillis();
			System.out.println(now + ": Lock release by " + Thread.currentThread().getName());
		}
	}
	
}