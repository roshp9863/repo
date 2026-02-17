package com.inn.dsa.kunal.subsetSubsequence;

public class RemoveAFromString {
    public static void main(String[] args) {
        String str = "baccad";
        StringBuilder s = new StringBuilder();
        String rs = removeAWithMySolution(str, s, 0);
        System.out.println("MY SOLUTION : "+ rs);
        rs = removeAWithOutputString(str,"");
        System.out.println("WITH OUTPUT : "+ rs);
        rs = removeAWithoutOutputString(str);
        System.out.println("WITHOUT OUTPUT : "+ rs);

    }

    private static String removeAWithMySolution(String str, StringBuilder op, int index) {
        
        if(index>=str.length()) {
            return op.toString();
        }

        if(str.charAt(index)!='a') {
            return removeAWithMySolution(str, op.append(str.charAt(index)), index+1);
        }else {
            return removeAWithMySolution(str, op, index+1);
        }

    }
    private static String removeAWithOutputString(String str, String op) {
        
        if(str.isEmpty()) {
            return op;
        }

        char ch = str.charAt(0);

        if(ch=='a') {
            return removeAWithOutputString(str.substring(1),op);
        }else {
            return removeAWithOutputString(str.substring(1), op+ch);
        }

    }

    private static String removeAWithoutOutputString(String str) {
        
        if(str.isEmpty()) {
            return "";
        }

        char ch = str.charAt(0);

        if(ch=='a') {
            return removeAWithoutOutputString(str.substring(1));
        }else {
            return ch + removeAWithoutOutputString(str.substring(1));
        }

    }


}
