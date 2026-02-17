package com.inn.multithreading.conceptandcoding;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerExample {

    // This class wraps a queue (buffer) that can be used by both producer and consumer
    static class SharedBuffer {
        private Queue<Integer> queue = new LinkedList<>();
        private final int capacity; // maximum number of items buffer can hold

        public SharedBuffer(int capacity) {
            this.capacity = capacity;
        }

        // produce an item and place it into the queue
        public synchronized void produce(int value) throws InterruptedException {
            // while buffer is full, wait
            while (queue.size() == capacity) {
                wait();  // release lock, go dormant until notified
            }

            // now there's space in the buffer, add the item
            queue.add(value);
            System.out.println("Produced: " + value);

            // notify waiting consumer threads
            notifyAll();
        }

        // consume an item from the queue
        public synchronized int consume() throws InterruptedException {
            // while buffer is empty, wait
            while (queue.isEmpty()) {
                wait();  // release lock, go dormant until notified
            }

            // now there's something in the buffer, remove it
            int value = queue.remove();
            System.out.println("Consumed: " + value);

            // notify waiting producer threads
            notifyAll();

            return value;
        }
    }

    // Producer thread: repeatedly produces items
    static class Producer implements Runnable {
        private SharedBuffer buffer;

        public Producer(SharedBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            int value = 0;
            try {
                while (true) {
                    // produce a value (in a real scenario, this could be reading data, computing, etc.)
                    buffer.produce(value);
                    value++;

                    // sleep a bit just to simulate "work"
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Producer interrupted");
            }
        }
    }

    // Consumer thread: repeatedly consumes items
    static class Consumer implements Runnable {
        private SharedBuffer buffer;

        public Consumer(SharedBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // consume an item from buffer
                    int val = buffer.consume();

                    // sleep a bit just to simulate "work"
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted");
            }
        }
    }

    public static void main(String[] args) {
        // Create a shared buffer with some capacity
        SharedBuffer buffer = new SharedBuffer(5);

        // Create producer and consumer tasks
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        // Create threads
        Thread producerThread = new Thread(producer, "ProducerThread");
        Thread consumerThread = new Thread(consumer, "ConsumerThread");

        // Start threads
        producerThread.start();
        consumerThread.start();
    }
}