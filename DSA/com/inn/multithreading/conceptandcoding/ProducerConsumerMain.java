package com.inn.multithreading.conceptandcoding;

import java.util.Stack;

class SharedResourcePC {
	
	Stack<Integer> items = new Stack<>();

	public synchronized void addItem(int item) {
		items.push(item);
		System.out.println("ITEM added by : "+ Thread.currentThread().getName());
		notifyAll();
	}

	public synchronized void consumeItem() {
		System.out.println("Consume ITEM method invoked by : "+ Thread.currentThread().getName());
		
		
		while(items.isEmpty()) {
			try {
				
				System.out.println("Thread : "+ Thread.currentThread().getName() + " is waiting now");
				wait();
			}catch(Exception e) {
				
			}
		}
		System.out.println("Consume ITEM  : "+ items.peek());
	    int item = items.pop();
	    
		
	}
	
}

class Producer implements Runnable{
	SharedResourcePC shared;
	int item;

	Producer(SharedResourcePC shared, int item) {
		this.shared = shared;
		this.item = item;
	}

	@Override
	public void run() {
		System.out.println("PRODUCER THREAD "+ Thread.currentThread().getName());
		try {
			Thread.sleep(5000l);
		}catch(Exception e) {
			
		}
		
		shared.addItem(item);
	}
	
}


class Consumer implements Runnable {
    SharedResourcePC shared;

    Consumer(SharedResourcePC shared) {
        this.shared = shared;
    }

    @Override
    public void run() {
        System.out.println("Consumer THREAD " + Thread.currentThread().getName());

        while (true) {
            shared.consumeItem();
            try {
                Thread.sleep(1000); // Optional sleep between consumptions
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}


public class ProducerConsumerMain {
	public static void main(String[] args) {
		System.out.println("MAIN METHOD STARTS");
		
		SharedResourcePC shared = new SharedResourcePC();
		
		
		//producer thread
		Thread producer1 = new Thread(new Producer(shared, 1));
		Thread producer2 = new Thread(new Producer(shared, 2));
		Thread producer3 = new Thread(new Producer(shared, 3));
		Thread producer4 = new Thread(new Producer(shared, 4));

		
		//consumer thread
		Thread consumer1 = new Thread(new Consumer(shared));
		
		producer1.start();
		producer2.start();
		producer3.start();
		producer4.start();
		consumer1.start();
		
		System.out.println("MAIN METHOD END");

		

	}
}
