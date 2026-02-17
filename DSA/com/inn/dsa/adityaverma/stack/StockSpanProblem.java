package com.inn.dsa.adityaverma.stack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class StockSpanProblem {
    public static void main(String[] args) {
        List<Integer> arr = Arrays.asList(100, 80, 60, 70, 60, 75, 85);
        List<Integer> rs = new ArrayList<>();
        Stack<Integer> st = new Stack<>();
        for(int i=0; i<arr.size(); i++) {
            while(!st.isEmpty() && st.peek()<=arr.get(i)) {
                st.pop();
            }
            if(st.isEmpty()) {
                rs.add(1);
            }else {
                rs.add(i - arr.indexOf(st.peek())); 
            }
            st.push(arr.get(i)); 
        }

        System.out.println(rs);
        
    }

}
