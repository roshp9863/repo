package com.inn.dsa.Strivers;

public class SubsetSum {
    public static void main(String[] args) {
        int sum = 7;
        int[] arr = {2, 5, 7};
        int n = arr.length;

        boolean[][] dp = new boolean[n][sum + 1];

        // Base case 1: sum = 0 is always true
        for (int i = 0; i < n; i++) {
            dp[i][0] = true;
        }

        // Base case 2: Only using arr[0], we can make sum = arr[0] if it's within bounds
        if (arr[0] <= sum) {
            dp[0][arr[0]] = true;
        }

        // Fill the rest of the dp table
        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= sum; j++) {
                boolean notTake = dp[i - 1][j]; // don't pick arr[i]
                boolean take = false;
                if (arr[i] <= j) {
                    take = dp[i - 1][j - arr[i]]; // pick arr[i]
                }
                dp[i][j] = take || notTake;
            }
        }

        for(int i=0; i<=sum; i++) {
            System.out.print(" " + i%10 + " ");
        }
        System.out.println();
        for(int i=0; i<n; i++) {
            for(int j=0; j<=sum; j++) {
                String s = (dp[i][j]==true) ? "T" : "F";
                System.out.print(" " + s + " ");
            }
            System.out.println();
        }
    }
}
