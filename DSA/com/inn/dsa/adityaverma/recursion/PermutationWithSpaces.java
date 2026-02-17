package com.inn.dsa.adityaverma.recursion;

public class PermutationWithSpaces {

    public static void main(String[] args) {
        permutationWithSpaces("a", "bc");
    }

    private static void permutationWithSpaces(String op, String ip) {
        if(ip.isEmpty()) {
            System.out.println(op);
            return;
        }

        String op1 = op + ip.charAt(0);
        String op2 = op + " " + ip.charAt(0);
        ip = ip.substring(1);
        permutationWithSpaces(op1, ip);
        permutationWithSpaces(op2, ip);


    }

}
