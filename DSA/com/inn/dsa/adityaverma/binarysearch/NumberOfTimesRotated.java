package com.inn.dsa.adityaverma.binarysearch;

public class NumberOfTimesRotated {
    public static void main(String[] args) {
        int[] arr = {5,1,2,3,4};

        int low = 0;
        int high = arr.length - 1;
        int n = arr.length;

        System.out.println("Start");

        while (low <= high) {
            // ✅ Case 1: Subarray already sorted → pivot at low
            if (arr[low] <= arr[high]) {
                System.out.println("Answer LOW = " + low);
                break;
            }

            int mid = low + (high - low) / 2;
            int prev = (mid + n - 1) % n;
            int next = (mid + 1) % n;

            if (arr[mid] <= arr[prev] && arr[mid] <= arr[next]) {
                System.out.println("Answer MID = " + mid); // 🟢 Pivot found
                break;
            }

            if (arr[low] <= arr[mid]) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        System.out.println("End");
    }
}