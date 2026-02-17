package com.inn.dsa.practice;

import java.util.Arrays;

public class PruductOfANumberExceptItself {
    public static void main(String[] args) {
        int[] arr = {1,2,3,4};

        int ppleft[] = new int[arr.length];
        int ppright[] = new int[arr.length];
        System.out.println(Arrays.toString(arr));

        ppleft[0] = 1;
        for(int i =1; i<arr.length; i++) {
            ppleft[i] = ppleft[i-1] * arr[i-1];
        }
        System.out.println(Arrays.toString(ppleft));

        ppright[arr.length-1] = 1;
        for(int i= arr.length-2; i>=0; i--) {
            ppright[i] = ppright[i+1] * arr[i+1];

        }
        System.out.println(Arrays.toString(ppright));

        for(int i=0; i<arr.length; i++) {
            arr[i] = ppleft[i] * ppright[i];
        }

        System.out.println(Arrays.toString(arr));
    }
}
