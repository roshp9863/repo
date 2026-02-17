package com.inn.dsa.adityaverma.recursion;

public class PermutationWithCaseChange {

    public static void main(String[] args) {
        permutationWithCaseChange("", "abc");
    }

    private static void permutationWithCaseChange(String op, String ip) {
        if(ip.isEmpty()) {
            System.out.println(op);
            return;
        }

        String firstChoice = ip.charAt(0) + "";
        String secondChoice = ip.charAt(0) + "";
        secondChoice = secondChoice.toUpperCase();
        String op1 = op + firstChoice;
        String op2 = op + secondChoice;
        

        ip = ip.substring(1);
        permutationWithCaseChange(op1, ip);
        permutationWithCaseChange(op2, ip);


    }


}
