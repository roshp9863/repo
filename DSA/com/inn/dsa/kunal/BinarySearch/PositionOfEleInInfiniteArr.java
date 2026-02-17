package com.inn.dsa.kunal.BinarySearch;

public class PositionOfEleInInfiniteArr {
    public static void main(String[] args) {
        int arr[] = {2,3,5,9,10,12,14,16,18};
        int target =10;    
        int low = 0;
        int high = 1;
        int ans = -1;
        while(target>arr[high]) {
                int temp = high+1;
                high = high+(high-low+1)*2;
                low = temp;
        }
        ans = binarysearch(arr, low, high, target);

        System.out.println(ans);
    }

    private static int binarysearch(int[] arr, int low, int high, int k) {
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
}
