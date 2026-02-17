package com.inn.dsa.practice;

public class MaxWindowSizeForSubArrayEqualsToK {
	public static void main(String[] args) {
		
		System.out.println(subarraySum(new int[] {4,1,1,1,2,3,5},5));
		
	}

	public static int subarraySum(int[] nums, int k) {
	    int i=0;
	    int j=0;
	    int sum=0;
	    int ws = 0;
	    while(j<nums.length) {
	        sum = sum+nums[j];
	        if(sum<k) {
	            j++;
	        }else if(sum==k){
	            if(ws<(j-i+1)) {
	                ws = j-i+1;
	            }
	            j++;

	        }else {

	            while(sum>k) {
	                sum = sum-nums[i];
	                i++;
	            }
	            j++;
	        } 
	    }
	    return ws;
	}

}

