package com.inn.dsa.adityaverma.binarysearch;

public class NearlySortedArray {

    public static void main(String[] args) {
        int[] arr = {5,10,30,20,40};
        int k =20;
        int n = arr.length;
        int low =0;
        int high=n-1;

        while (low<=high) {
            int mid = low + (high-low)/2;

            if(arr[mid]==k) {
                System.out.println(mid);
                break;
            }else if(mid-1>=low && arr[mid-1] == k) {
                System.out.println(mid-1);
                break;
            }else if(mid+1<=high && arr[mid+1] == k) {
                System.out.println(mid+1);
                break;
            }else if(arr[mid] > k) {
                high = mid -2;
            }else {
                low = mid +2;
            }
            
        }
    }

}
