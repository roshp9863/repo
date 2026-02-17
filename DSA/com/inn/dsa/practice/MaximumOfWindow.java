package com.inn.dsa.practice;

import java.util.ArrayList;
import java.util.List;

public class MaximumOfWindow {

    public static void main(String[] args) {
        int arr[] = {23,15,16,12,4,2,10,9,13,7};
        int n = arr.length;
        int k = 3;
        int i=0;
        int max = Integer.MIN_VALUE;
        List<Integer> rs = new ArrayList<>(); 
        while(i<k) {
            max = Math.max(max, arr[i]);
            i++;
        }
        rs.add(max);
        int j=0;
        while(i<n) {
            if(max==arr[j]) {
                max = Integer.MIN_VALUE;  // Reset max before recomputing

                int b=j+1;
                while(b<=i) {
                    max = Math.max(max, arr[b]);
                    b++;
                }
            }else {
                max = Math.max(max, arr[i]);

            }
            i++;
            j++;
            rs.add(max);
        }

        System.out.println(rs);


    }

}
