package com.inn.dsa.adityaverma.recursion;

public class GenerateBalancedParenthesis {

    public static void main(String[] args) {
        generateBalancedParenthesis(3, 3, "");
    }

    private static void generateBalancedParenthesis(int open, int closed, String op) {
        if(open==0 && closed==0) {
            System.out.println(op);
            return;
        }

        if(open>0) {
            String op1 = op + "(";
            generateBalancedParenthesis(open-1, closed, op1);
        }

        if(closed>open) {
            String op1 = op + ")";
            generateBalancedParenthesis(open, closed-1, op1);
        }
    }

}
