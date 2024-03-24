package com.inn.dsa.practice;
class MaximumSubarray {
    public static void main(String[] args) {
        int[] arr = {3,2,14,5,6,8};
        int k=3;
        // int i=0;
        // int j=arr.length;
        // int max = Integer.MIN_VALUE;
        // int sum=0;
        // int m=0;
        // while(i<j) {
        //     if(m==k) {
        //         if(max<sum) {
        //             max= sum;
        //         }
        //         sum=sum-arr[i-3];
        //         m=m-1;
        //     }else {
        //         sum=sum+arr[i];
        //         m++;
        //         i++;
        //     }
        // }

        // System.out.println(max);

        

        int maxSum = Integer.MIN_VALUE;
        int currentSum = 0;

        // for (int i = 0; i < arr.length; i++) {
        //     currentSum += arr[i];

        //     if (i >= k - 1) {
        //         maxSum = Math.max(maxSum, currentSum);
        //         currentSum -= arr[i - k + 1];
        //     }
        // }
        
        int i=0;
        int j=0;
        while(j<arr.length) {
            currentSum = currentSum+arr[j];
            if(j-i+1<k) {
                j++;
            }else if(j-i+1==k) {
                maxSum = Math.max(maxSum, currentSum);
                currentSum = currentSum-arr[i];
                i++;
                j++;
            }
        }   

        System.out.println(maxSum);

    }
}