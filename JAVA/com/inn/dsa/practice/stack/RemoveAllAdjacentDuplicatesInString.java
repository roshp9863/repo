package com.inn.dsa.practice.stack;

import java.util.*;
 
public class RemoveAllAdjacentDuplicatesInString {
	public static void main(String[] args) {
		Solution s = new Solution();
		System.out.println(s.removeDuplicates("abbaca"));
	}

}
class Solution {
    public String removeDuplicates(String s) {
        String rs = "";
        Stack<Character> stack = new Stack<>();
        int i=0;
        while(i<s.length()) {
            if(!stack.isEmpty() && stack.peek()==s.charAt(i)) {
                stack.pop();
            }else {
                stack.push(s.charAt(i));
            }
            i++;
        }

        while(!stack.isEmpty()) {
            rs = stack.peek() + rs;
            stack.pop();
        }
        return rs;
    }
}