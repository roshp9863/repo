package com.inn.dsa.adityaverma.recursion;

public class BinaryNumberCreation {
    public static void main(String[] args) {
        binaryNumberCreation(3, "");
    }

    private static void binaryNumberCreation(int ip, String op) {
        if(ip==0) {
            System.out.println(op);
            return;
        }

        String op1 = op + "1";
        String op2 = op + "0";
        binaryNumberCreation(ip-1, op2);
        binaryNumberCreation(ip-1, op1);

    }

}
