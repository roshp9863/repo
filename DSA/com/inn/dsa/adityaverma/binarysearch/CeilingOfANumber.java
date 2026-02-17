package com.inn.dsa.adityaverma.binarysearch;

public class CeilingOfANumber {
    public static void main(String[] args) {
        int arr[] = {2,3,5,9,10,12,14,16,18};
        int target = 1;
        int low =0;
        int high = arr.length-1;
        int ans =arr[0];
        while(low<=high) {
            int mid = low + (high - low)/2;

            if(arr[mid] == target) {
                ans=arr[mid];
                break;
            }else if(arr[mid] > target) {
                ans = arr[mid];
                high = mid-1;
            }else {
                low = mid+1;
            }
        }

        System.out.println(ans);
    }
}
