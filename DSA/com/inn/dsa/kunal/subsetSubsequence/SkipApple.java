package com.inn.dsa.kunal.subsetSubsequence;

public class SkipApple {
    public static void main(String[] args) {
        String str = "adbhgsdappledhsgja";
        String rs = skipapple(str);
        System.out.println(rs);
    }

    private static String skipapple(String str) {
        if(str.isEmpty()) {
            return "";
        }
        if(str.startsWith("apple")) {
            return skipapple(str.substring(5)); //apple has 5 characters
        }else {
            return str.charAt(0) + skipapple(str.substring(1));
        }
    }
}
