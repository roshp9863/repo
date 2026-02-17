package com.inn.dsa.DynamicProgramming;

import java.util.Arrays;

public class RobHouse {

    public static void main(String[] args) {
        int arr[] = {7, 2,3,9,1};
        int dp[] = new int[arr.length];
        Arrays.fill(dp, -1);
        int ans = rob(arr, 0, dp);
        System.out.println(ans);
    }

    private static int rob(int[] arr, int i, int[] dp) {
        if(i>=arr.length) {
            return 0;
        }
        if(dp[i]!=-1) {
            return dp[i];
        }

        int ans= Math.max(arr[i] + rob(arr, i+2,dp), rob(arr,i+1,dp));
        dp[i] = ans;
        return ans;
    }


}
