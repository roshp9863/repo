package com.inn.dsa.kunal.BinarySearch;


//https://leetcode.com/problems/find-in-mountain-array/description/

//first find peak
//binary search on ascending array if found return else binary search in descending array
public class HardFindInMountainArray {
    public static void main(String[] args) {

        int arr[] = {1,2,3,4,5,3,1};
        int target = 3;

        int ans = findInMountainArray(target, arr);
        System.out.println(ans);
        
    }

    public static int findInMountainArray(int target, int[] m) {
        int low=0;
        int high = m.length;
        while(low<high) {
            int mid = low + (high-low)/2;
            if(m[mid] > m[mid+1]) {
                high = mid;
            }else {
                low = mid +1;
            }
        }

        int peak = low;
        int index = binarySearchAsc(m, 0, peak, target);
        if(index == -1) {
            index = binarySearchDes(m, peak, m.length-1, target);
        }

        return index;
    }

    public static int binarySearchAsc(int[] m, int low, int high, int k) {
        while(low<=high) {
            int mid = low + (high-low)/2;

            if(m[mid]==k) {
                return mid;
            }else if(m[mid]>k) {
                high = mid-1;
            }else {
                low = mid+1;
            }
        }
        return -1;
    }

    public static int binarySearchDes(int[] m, int low, int high, int k) {
        while(low<=high) {
            int mid = low + (high-low)/2;

            if(m[mid]==k) {
                return mid;
            }else if(m[mid]>k) {
                low = mid+1;
            }else {
                high = mid-1;
            }
        }
        return -1;
    }

}
