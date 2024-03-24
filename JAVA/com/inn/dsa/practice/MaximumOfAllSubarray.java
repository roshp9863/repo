package com.inn.dsa.practice;
import java.util.List;
import java.util.ArrayList;
class MaximumOfAllSubarray {
    public static void main(String[] args) {
        int[] arr = {1, 3, -1, -3, 5, 3, 6, 7};
        List<Integer> rs = new ArrayList<>();
        int len = arr.length;
        int k = 3;

        int i=0;
        int j=0;

        while(j<len) {
            if(j-i+1<k) {
                j++;
            }else if(j-i+1==k) {
                int max = Integer.MIN_VALUE;
                for(int x=i; x<=j; x++) {
                    if(max<arr[x]) {
                        max = arr[x];
                    }
                }
                rs.add(max);
                i++;
                j++;
            }
        }
        System.out.println(rs);
    }
}