package com.inn.dsa.adityaverma.recursion.RecursionProblems;

import java.util.ArrayList;
import java.util.List;

//https://www.geeksforgeeks.org/problems/game-of-death-in-a-circle1840/1

public class JosephsProblem {

    public static void main(String[] args) {
        List<Integer> arr = new ArrayList<>();
        for(int i=0; i<40; i++)
            arr.add(i+1);
        int k =7;
        josephsProblem(arr, k-1, 0);
    }

    private static void josephsProblem(List<Integer> ip, int k, int index) {
        if(ip.size()==1) {
            System.out.println(ip.get(0));
            return;
        }
        index = (index + k) % ip.size(); // modulo to find the index in which we need to kill the person
        ip.remove(index);
        josephsProblem(ip, k, index);
    }

}
