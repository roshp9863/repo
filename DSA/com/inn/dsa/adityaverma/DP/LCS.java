package com.inn.dsa.adityaverma.DP;

public class LCS {
    public static void main(String[] args) {
        String x = "abcde";
        String y = "abced";

        int m = x.length();
        int n = y.length();

        int[][] dp = new int[m+1][n+1];

        for(int i=1; i<=m; i++) {
            for(int j=1; j<=n; j++) {
                if(x.charAt(i-1)==y.charAt(j-1)) {
                    dp[i][j] = 1 + dp[i-1][j-1];
                }else {
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
                }
            }
        }

        System.out.println("Longest Common Subsequence : "+ dp[m][n]);


        int[][] dp1 = new int[m+1][n+1];
        int maxLength = 0;
        for(int i=1; i<=m; i++) {
            for(int j=1; j<=n; j++) {
                if(x.charAt(i-1)==y.charAt(j-1)) {
                    dp1[i][j] = 1 + dp1[i-1][j-1];
                    maxLength = Math.max(maxLength, dp1[i][j]);
                }else {
                    dp1[i][j] = 0;
                }
            }
        }

        System.out.println("Longest Common Substring : "+ maxLength);

        int i=m;
        int j=n;
        StringBuilder sb = new StringBuilder();
        while(i>0 && j>0) {
            if(x.charAt(i-1)==y.charAt(j-1)) {
                sb.append(x.charAt(i-1));
                i--;
                j--;
            }else {
                if(dp[i-1][j] > dp[i][j-1]) {
                    i--;
                }else {
                    j--;
                }
            }
        }

        System.out.println("Longest Common Subsequence Print : "+  sb.reverse().toString());


    }
}
