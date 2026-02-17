package com.inn.dsa.adityaverma.binarysearch;

public class FloorOfANumber {
    public static void main(String[] args) {
        int arr[] = {2,3,5,9,10,12,14,16,18};
        int target = 9;
        int low =0;
        int high = arr.length-1;
        int ans =-1;
        while(low<=high) {
            int mid = low + (high - low)/2;

            if(arr[mid] == target) {
                ans=mid;
                break;
            }else if(arr[mid] < target) {
                ans = mid;
                low = mid+1;
            }else {
                high = mid-1;
            }
        }

        System.out.println(ans);
    }

}
