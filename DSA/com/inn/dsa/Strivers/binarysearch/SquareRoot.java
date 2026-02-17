package com.inn.dsa.Strivers.binarysearch;

public class SquareRoot {
    public static void main(String[] args) {
        int number = 50;

        int low = 1;
        int high = number;
        int ans = 0;
        while(low<=high) {
            int mid = low + (high-low)/2;
            if(mid*mid == number) {
                ans = mid;
            }else if (mid*mid < number) {
                ans = mid;
                low = mid+1;
            }else {
                high = mid-1;
            }
        }
        System.out.println(ans);

    }
}
