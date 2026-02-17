package com.inn.dsa.kunal.subsetSubsequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubSeq {

    public static void main(String[] args) {

        subseq("", "abc");
        List<String> rs = subseqRet("", "abc");
        System.out.println("RETURN RS "+ rs);
        subseqArr(new ArrayList<Integer>(), new int[] {1,2,3});

        
    }


    private static void subseq(String processed, String unprocessed) {
        if(unprocessed.isEmpty()) {
            System.out.println(processed);
            return;
        }

        char ch = unprocessed.charAt(0);
        subseq(processed+ch, unprocessed.substring(1)); //take it
        subseq(processed, unprocessed.substring(1)); //ignore it
    }

    private static List<String> subseqRet(String processed, String unprocessed) {
        if(unprocessed.isEmpty()) {
            List<String> ls = new ArrayList<>();
            ls.add(processed);
            return ls;
        }
        char ch = unprocessed.charAt(0);
        List<String> left = subseqRet(processed+ch, unprocessed.substring(1));
        List<String> right = subseqRet(processed, unprocessed.substring(1));

        left.addAll(right);
        return left;

    }

    private static void subseqArr(List<Integer> processed, int[] unprocessed) {
        if(unprocessed.length==0) {
            System.out.println(processed);
            return;
        }

        int ch = unprocessed[0];
        List<Integer> including = new ArrayList<>(processed);
        including.add(ch);

        subseqArr(including, Arrays.copyOfRange(unprocessed, 1, unprocessed.length)); //take it

        List<Integer> excluding = new ArrayList<>(processed);
        subseqArr(excluding, Arrays.copyOfRange(unprocessed, 1, unprocessed.length)); //ignore it
    }



}
