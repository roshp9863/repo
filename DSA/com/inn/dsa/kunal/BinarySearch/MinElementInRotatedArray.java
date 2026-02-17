package com.inn.dsa.kunal.BinarySearch;

import java.util.Arrays;
import java.util.List;

public class MinElementInRotatedArray {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(4,5,6,0,1,2,3);
        int pivot = findPivot(list);
        if(pivot==list.size()-1) {
            System.out.println("Smallest element " + list.get(0));
            System.out.println("Number of rotation " + 0);

        }else {
            System.out.println("Smallest element "+ list.get(pivot+1));
            System.out.println("Number of rotation " + (pivot+1));
        }    

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

            if (A.get(mid) >= A.get(low)) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;  
    }


}
