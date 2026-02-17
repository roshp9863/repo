package com.inn.dsa.adityaverma.stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


//when its right greater start from last index, peek<=arr[i]

public class NextGreaterToRight {
    public static void main(String[] args) {
        int[] arr = {1,3,2,4};
        List<Integer> rs = new ArrayList<>();
        Stack<Integer> st = new Stack<>();
        for(int i=arr.length-1; i>=0; i--) {
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
        Collections.reverse(rs);
        System.out.println(rs);

    }

}
