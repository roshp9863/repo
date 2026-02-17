package com.inn.dsa.kunal.BinarySearch;

import java.util.Arrays;
import java.util.List;

public class SearchInRotatedArrayUniqueElement {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(4,5,6,0,1,2,3);

        int ans = search(list, 6);

        System.out.println(ans);
        

        
    }
    public static int search(List<Integer> A, int B) {
        int index = -1;
        int n = A.size();

        int pivot = findPivot(A);
        if (pivot != -1 && A.get(pivot) == B) {
            return pivot;
        }

        if (pivot == -1) {
            return binarySearch(A, 0, n - 1, B);
        }

        index = binarySearch(A, 0, pivot - 1, B);
        if (index != -1) {
            return index;
        }

        return binarySearch(A, pivot + 1, n - 1, B);
    }

    public static int findPivot(List<Integer> A) {
        int low = 0;
        int high = A.size() - 1;              

        while (low <= high) {                
            int mid = low + (high - low) / 2;

            if (mid < high && A.get(mid) > A.get(mid + 1)) {
                return mid;
            }
            if (mid > low && A.get(mid) < A.get(mid - 1)) {
                return mid - 1;
            }

            if (A.get(mid) <= A.get(low)) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return -1;  
    }
    
    public static int binarySearch(List<Integer> A, int low, int high, int B) {
        while(low<=high) {
            int mid = low + (high-low)/2;

            if(A.get(mid)==B) {
                return mid;
            }else if(A.get(mid)>B) {
                high=mid-1;
            }else {
                low=mid+1;
            }
        }

        return -1;
    }
}
