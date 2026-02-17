package com.inn.dsa.adityaverma.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class NearestGreaterToLeft { //NearestGreaterToLeft
    public static void main(String[] args) {
        int[] arr = {100, 80, 60, 70, 60, 75, 85};
        List<Integer> rs = new ArrayList<>();
        Stack<Integer> st = new Stack<>();
        for(int i=0; i<arr.length; i++) {
            while(!st.isEmpty() && st.peek()<=arr[i]) {
                st.pop();
            }
            if(st.isEmpty()) {
                rs.add(-1);
            }else {
                rs.add(st.peek()); 
            }
            st.push(arr[i]); 
        }
        System.out.println(rs);

    }
}
