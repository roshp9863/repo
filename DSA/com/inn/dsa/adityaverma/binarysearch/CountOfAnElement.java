package com.inn.dsa.adityaverma.binarysearch;

public class CountOfAnElement {

    public static void main(String[] args) {
        int[] arr = {2, 4, 5, 7, 9, 10, 10, 10, 12};
        int k=10;
        int low =0;
        int high = arr.length-1;
        int ans[] = new int[2];

        //FirstOccurrence
        while(low<=high) {
            int mid = low + (high-low)/2;

            if(arr[mid]==k) {
                ans[0] = mid;
                high = mid -1;
            }else if(arr[mid]>k) {
                high = mid -1;
            }else {
                low = mid +1;
            }
        }


        low = 0;
        high = arr.length-1;

        //last occurrence    
        while(low<=high) {
            int mid = low + (high-low)/2;

            if(arr[mid]==k) {
                ans[1] = mid;
                low = mid +1;
            }else if(arr[mid]>k) {
                high = mid -1;
            }else {
                low = mid +1;
            }
        }

        System.out.println("FIRST OCC : "+ ans[0] + " LAST OCC : "+ ans[1]);
        System.out.println("COUNT OF ELEMENT : "+ (ans[1] - ans[0] + 1));
    }

}
