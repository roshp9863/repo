package com.inn.dsa.kunal.BinarySearch;

public class SearchInRotatedArrayDuplicateElement {

    public static void main(String[] args) {
        int[] nums = {12,12,12,12,12,4,5,6};
        int target=5;
        if(nums.length==1) {
            if(nums[0]==target) {
                System.out.println(true);
            }else {
                System.out.println(false);
            }
        }else {
            int pivot = findPivot(nums);
            System.out.println("pivot : "+pivot);
            int index =-1;
            
            index = binarySearch(nums, 0, pivot, target);
            if(index==-1) {
                index = binarySearch(nums, pivot+1, nums.length-1, target);
            }
            if(index==-1) {
                System.out.println(false);
            }else {
                System.out.println(true);
            }

        }
        
    }


    public static int findPivot(int[] arr) {
        int low = 0;
        int high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (mid < high && arr[mid] > arr[mid + 1]) {
                return mid;
            }
            if (mid > low && arr[mid] < arr[mid - 1]) {
                return mid-1;
            }

            // if elements at middle, start, end are equal then just skip the duplicates
            if (arr[mid] == arr[low] && arr[mid] == arr[high]) {
                // skip the duplicates
                // NOTE: what if these elements at start and end were the pivot??
                // check if start is pivot
                if (low < high && arr[low] > arr[low + 1]) {
                    return low;
                }
                low++;

                // check whether end is pivot
                if (high > low && arr[high] < arr[high - 1]) {
                    return high - 1;
                }
                high--;
            }
            // left side is sorted, so pivot should be in right
            else if(arr[low] < arr[mid] || (arr[low] == arr[mid] && arr[mid] > arr[high])) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static int binarySearch(int[] nums, int low, int high, int target) {
        while(low<=high) {
            int mid = low + (high-low)/2;
            if(nums[mid]==target) {
                return mid;
            }else if(nums[mid]<target) {
                low = mid+1;
            }else {
                high = mid -1;
            }
        }
        return -1;
    }


}
