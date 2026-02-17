package com.inn.multithreading.practice;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MergeSortUsingCallable {
    // merge sort using callables and futures
    private static final int THRESHOLD = 4; // switch to sequential sort for small slices

    public static void main(String[] args) {
        int[] array = {38, 27, 43, 3, 9, 82, 10};
        System.out.println("Original Array:");
        printArray(array);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Future<int[]> sortedFuture = executorService.submit(new MergeSortTask(array, 0, array.length - 1, executorService));

        try {
            int[] sorted = sortedFuture.get();
            System.out.println("Sorted Array:");
            printArray(sorted);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Sorting was interrupted");
        } catch (ExecutionException e) {
            System.out.println("Sorting failed: " + e.getCause());
        } finally {
            executorService.shutdown();
        }
    }

    private static class MergeSortTask implements Callable<int[]> {
        private final int[] array;
        private final int left;
        private final int right;
        private final ExecutorService executorService;

        MergeSortTask(int[] array, int left, int right, ExecutorService executorService) {
            this.array = array;
            this.left = left;
            this.right = right;
            this.executorService = executorService;
        }

        @Override
        public int[] call() throws Exception {
            int length = right - left + 1;
            if (length <= THRESHOLD) {
                int[] slice = Arrays.copyOfRange(array, left, right + 1);
                Arrays.sort(slice);
                return slice;
            }

            int mid = (left + right) / 2;

            Future<int[]> leftFuture = executorService.submit(new MergeSortTask(array, left, mid, executorService));
            Future<int[]> rightFuture = executorService.submit(new MergeSortTask(array, mid + 1, right, executorService));

            int[] leftSorted = leftFuture.get();
            int[] rightSorted = rightFuture.get();

            return merge(leftSorted, rightSorted);
        }
    }

    private static int[] merge(int[] left, int[] right) {
        int[] merged = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                merged[k++] = left[i++];
            } else {
                merged[k++] = right[j++];
            }
        }

        while (i < left.length) {
            merged[k++] = left[i++];
        }

        while (j < right.length) {
            merged[k++] = right[j++];
        }

        return merged;
    }

    private static void printArray(int[] array) {
        for (int value : array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
}
