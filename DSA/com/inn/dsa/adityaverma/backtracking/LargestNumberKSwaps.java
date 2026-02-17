package com.inn.dsa.adityaverma.backtracking;

public class LargestNumberKSwaps {

    static String max = "";
    public static void main(String[] args) {
        String num = "1234567";
        StringBuilder sb = new StringBuilder(num);
        max = num; 
        int k=4;
        makeItLargeNum(sb, k, 0);

        System.out.println("Largest number after " + k + " swaps: " + max);


    }

    private static void makeItLargeNum(StringBuilder num, int k, int start) {
        if (k == 0 || start == num.length() - 1) return;

        // Fix: find max digit in remaining string (from start to end)
        char maxChar = num.charAt(start);
        for (int i = start + 1; i < num.length(); i++) {
            if (num.charAt(i) > maxChar) {
                maxChar = num.charAt(i);
            }
        }

        // If no better digit exists, skip swap and go forward
        if (maxChar == num.charAt(start)) {
            makeItLargeNum(num, k, start + 1);
            return;
        }

        for (int i = num.length() - 1; i > start; i--) {
            if (num.charAt(i) == maxChar) {
                swap(num, i, start);

                if (num.toString().compareTo(max) > 0) {
                    max = num.toString();
                }

                makeItLargeNum(num, k - 1, start + 1);
                swap(num, i, start); // backtrack
            }
        }
    }

    private static void swap(StringBuilder sb, int i, int j) {
        char temp = sb.charAt(i);
        sb.setCharAt(i, sb.charAt(j));
        sb.setCharAt(j, temp);
    }
}
