package com.inn.dsa.adityaverma.recursion.RecursionProblems;

import java.util.HashSet;
import java.util.Set;

public class PermutationAditya {

    public static void main(String[] args) {
        permutation("", "abc");
        System.out.println();
        permutationWithDuplicates("", "aac");
    }

    public static void permutation(String op, String ip) {

        if(ip.isEmpty()) {
            System.out.println(op);
            return;
        }
        
        for(int i=0; i<ip.length(); i++) {
            String newIp = ip.substring(0,i) + ip.substring(i+1);
            String newOp = op + ip.charAt(i);

            permutation(newOp, newIp);
        }

    }

    public static void permutationWithDuplicates(String op, String ip) {
        if (ip.isEmpty()) {
            System.out.println(op);
            return;
        }

        Set<Character> used = new HashSet<>(); // for pruning

        for (int i = 0; i < ip.length(); i++) {
            char ch = ip.charAt(i);
            if (used.contains(ch)) {
                continue; // cut the branch if this char is already used at this level
            }
            used.add(ch); // mark this char used for this level

            String newIp = ip.substring(0, i) + ip.substring(i + 1);
            String newOp = op + ch;
            permutationWithDuplicates(newOp, newIp);
        }
    }



}
