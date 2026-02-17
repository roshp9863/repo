package com.inn.dsa.stack;

import java.util.Stack;

public class DoubleCharacterTrouble {
    public static void main(String[] args) {
        char[] ch = {'a', 'b', 'c', 'c', 'b', 'd', 'd', 'd', 'b', 'c', 'a', 'a', 'b'};
        Stack<Character> st = new Stack<>();

        for(int i=0; i<ch.length; i++) {
            if(!st.isEmpty() && st.peek()==ch[i]) {
                st.pop();
            }else {
                st.push(ch[i]);
            }
        }
        StringBuilder s = new StringBuilder();
        while(!st.isEmpty()) {
            s.append(st.peek());
            st.pop();
        }

        System.out.println(s.reverse());
    }
}
