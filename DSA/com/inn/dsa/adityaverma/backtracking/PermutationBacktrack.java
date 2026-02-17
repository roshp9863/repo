package com.inn.dsa.adityaverma.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermutationBacktrack {
    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> rs = new ArrayList<>();
        List<Integer> A = Arrays.asList(1,2,3);
        solved(A,0,rs);

        int n=4;
        ArrayList<ArrayList<Integer>> rs1 = new ArrayList<>();
        climbing(n, new ArrayList<Integer>(), rs1);
        System.out.println(rs1);
    }




    private static void climbing(int n, ArrayList<Integer> ls, ArrayList<ArrayList<Integer>> rs1) {
        if(n==0) {
            rs1.add(new ArrayList<>());
            return;
        }

        if(n>0) {
            ls.add(1);
            climbing(n-1, ls, rs1);
            ls.remove(ls.size()-1);
        }
        if(n>=2) {
            ls.add(2);
            climbing(n-2, ls, rs1);
            ls.remove(ls.size()-1);

        }
    }




    private static void solved(List<Integer> ip, int start, ArrayList<ArrayList<Integer>> result) {
        if(start==ip.size()) {
            result.add(new ArrayList<>(ip)); 
            return;
        }

        for(int i=start; i<ip.size(); i++) {
            swap(ip, start, i);
            solved(ip,start+1,result);
            swap(ip, start, i);
        }
    }

    private static void swap(List<Integer> ip, int i, int j) {
        int temp = ip.get(i);
        ip.set(i, ip.get(j));
        ip.set(j, temp);
    }

}
