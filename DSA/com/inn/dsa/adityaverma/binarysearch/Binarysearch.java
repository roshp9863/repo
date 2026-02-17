package com.inn.dsa.adityaverma.binarysearch;

public class Binarysearch {

    public static void main(String[] args) {
        int[] arr = {1,3 ,4 ,6 ,7, 9, 11};
        int k =6;

        int low = 0;
        int high = arr.length-1;

        while(low<=high) {
            int mid = low + (high - low)/2;

            if(arr[mid] == k) {
                System.out.println(mid);
                break;
            }else if(arr[mid] > k) {
                high = mid - 1;
            }else {
                low = mid + 1;
            }
        }

    }
}
