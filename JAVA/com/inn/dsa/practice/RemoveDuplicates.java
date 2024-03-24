package com.inn.dsa.practice;
import java.util.*;
class RemoveDuplicates {
    public static void main(String[] args) {
        int[] arr = {1,1,1,2,2,3,4,4,5};
        doUsingBruteForce(arr);
        doUsingOptimal(arr);
    }

    private static void doUsingOptimal(int[] arr) {
        int j=0;
        for(int i=1;i<arr.length;i++) {
            if(arr[i]!=arr[j]) {
                arr[j+1] = arr[i];
                j++;
            }
        }
        System.out.println(j+1);
        System.out.println(Arrays.toString(arr));
    }

    private static void doUsingBruteForce(int[] arr) {
        //time O(n)  space O(n)
        Set<Integer> set = new HashSet<>();
        for(int i=0;i<arr.length;i++) {
            set.add(arr[i]);
        }
        System.out.println(set.size());
        System.out.println(set);
    }

}