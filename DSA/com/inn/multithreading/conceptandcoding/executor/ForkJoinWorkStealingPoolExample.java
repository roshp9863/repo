package com.inn.multithreading.conceptandcoding.executor;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class ForkJoinWorkStealingPoolExample {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        Future<Integer> futureObj = forkJoinPool.submit(
            new ComputeSumTask(1, 100) // Task to compute the sum of numbers from 1 to 100
            // the task will put in SubmissionQueue
        );

        try {
            Integer result = futureObj.get();
            System.out.println("Sum: " + result);
        } catch (Exception e) {
            // TODO: handle exception
        }

        
    }

}

class ComputeSumTask  extends RecursiveTask<Integer> {
    private int start;
    private int end;

    public ComputeSumTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= 4) { // Threshold for splitting the task
            int sum = 0;
            for (int i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            ComputeSumTask leftTask = new ComputeSumTask(start, mid);
            ComputeSumTask rightTask = new ComputeSumTask(mid + 1, end);
            leftTask.fork(); 
            // Fork the left task to run in parallel
            // The fork() method is used to start the task asynchronously.
            // The fork() method is a non-blocking call that starts the task in a separate thread.
            rightTask.fork(); 
            // Fork the right task to run in parallel

            // Join the results of the left and right tasks
            // The join() method blocks until the task is complete
            // and returns the result of the computation.
            // The join() method is used to retrieve the result of a task that has been forked.
            // The join() method is a blocking call that waits for the task to complete and returns the result.
            // The join() method is used to retrieve the result of a task that has been forked.
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();

            // Combine the results
            return leftResult + rightResult;

        }
    }



    
}
