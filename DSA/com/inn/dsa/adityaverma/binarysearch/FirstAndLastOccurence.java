package com.inn.dsa.adityaverma.binarysearch;

public class FirstAndLastOccurence {
    public static void main(String[] args) {
        int[] arr = {2, 4, 5, 7, 9, 10, 10, 10, 12};
        int k=10;
        int low =0;
        int high = arr.length-1;
        int ans=-1;

        //FirstOccurrence
        while(low<=high) {
            int mid = low + (high-low)/2;

            if(arr[mid]==k) {
                ans = mid;
                high = mid -1;
            }else if(arr[mid]>k) {
                high = mid -1;
            }else {
                low = mid +1;
            }
        }

        System.out.println(ans);

        low = 0;
        high = arr.length-1;

        //last occurrence    
        while(low<=high) {
            int mid = low + (high-low)/2;

            if(arr[mid]==k) {
                ans = mid;
                low = mid +1;
            }else if(arr[mid]>k) {
                high = mid -1;
            }else {
                low = mid +1;
            }
        }

        System.out.println(ans);


    }
}
