package com.inn.dsa.adityaverma.recursion;

public class Subsequence {

    public static void main(String[] args) {
        subseq("", "123");

    }

    private static void subseq(String op, String ip) {

        if(ip.isEmpty()) {
            System.out.println(op);
            return;
        }

        String op1 = op;
        String op2 = op + ip.charAt(0);
        ip = ip.substring(1);

        subseq(op1, ip);
        subseq(op2, ip);

    }
}
