package com.inn.dsa.adityaverma.binarysearch;

public class FindEleInRotatedSortedArray {

    public static void main(String[] args) {
        
        int[] arr = {11,12,15,18,2,5,6,8};
        int n = arr.length;
        int low = 0;
        int high = n - 1;
        int k=15;

        int minElementIndex = binarysearchForMinEle(arr, low, high, n);

        int index = binarysearch(arr, low, minElementIndex-1, n, k);
        if(index<0)
            index = binarysearch(arr, minElementIndex, high, n, k);
        

        System.out.println(index);

    }

    private static int binarysearch(int[] arr, int low, int high, int n, int k) {

        while(low<=high) {
            int mid = low + (high - low)/2;

            if(arr[mid] == k) {
                return mid;
            }else if(arr[mid] > k) {
                high = mid - 1;
            }else {
                low = mid + 1;
            }
        }
        return -1;

    }

    private static int binarysearchForMinEle(int[] arr, int low, int high, int n) {
        while (low <= high) {
            // ✅ Case 1: Subarray already sorted → pivot at low
            if (arr[low] <= arr[high]) {
                return low;
            }

            int mid = low + (high - low) / 2;
            int prev = (mid + n - 1) % n;
            int next = (mid + 1) % n;

            if (arr[mid] <= arr[prev] && arr[mid] <= arr[next]) {
                return mid;
            }

            if (arr[low] <= arr[mid]) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;

    }

}
