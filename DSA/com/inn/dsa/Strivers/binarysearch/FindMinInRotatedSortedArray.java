package com.inn.dsa.Strivers.binarysearch;;

public class FindMinInRotatedSortedArray {
    public static void main(String[] args) {
        int[] arr = {6, 1, 2, 3, 4, 5};
        int low =0; 
        int high = arr.length-1;
        int ans = Integer.MAX_VALUE;
        if (arr[low] <= arr[high]) {
            ans = arr[low];
        }else {
            while(low<=high) {
                int mid = low + (high-low)/2;

                if (arr[low] <= arr[mid]) {
                    ans = Math.min(ans, arr[low]);
                    low = mid + 1;

                } else {
                    ans = Math.min(ans, arr[mid]);
                    high = mid - 1;
                }

            }
        }
        System.out.println(ans);
    }
}
