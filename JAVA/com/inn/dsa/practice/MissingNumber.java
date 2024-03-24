package com.inn.dsa.practice;
class MissingNumber {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // this is nice solution but not for bigger numbers like 10^5
        int[] arr = {1,2,4,5,6};
        int sum=0;
        int n = arr.length+1;
        for(int a: arr) {
            sum = sum+a;
        }
        int validSum = (n*(n+1))/2;
        System.out.println(validSum-sum); 

        int xor1=0, xor2=0;
        int n1 = arr.length-1;
        for(int i=0; i<n1;i++) {
            xor2 = xor2^arr[i];
            xor1 = xor1^(i+1);
        }
        xor1 = xor1^arr.length;
        System.out.println(xor1^xor2);

    }
}