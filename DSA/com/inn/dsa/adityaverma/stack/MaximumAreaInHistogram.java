package com.inn.dsa.adityaverma.stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class MaximumAreaInHistogram {
    public static void main(String[] args) {
        int[] arr = {6,2,5,4,5,1,6};
        int max = Integer.MIN_VALUE;
        List<Integer> ngl = nearestSmallerLeft(arr);
        List<Integer> ngr = nearestSmallerRight(arr);

        int area =0;
        List<Integer> width = new ArrayList<>();
        for(int i=0; i<ngl.size(); i++) {
            width.add(Math.abs(ngr.get(i)-ngl.get(i)-1));
        }

        for(int i=0; i<arr.length; i++) {
            area = width.get(i)*arr[i];
            max = Math.max(area, max);
        }

        System.out.println("MAX : "+max);
    }

    private static List<Integer> nearestSmallerRight(int[] arr) {
        List<Integer> rs = new ArrayList<>();
        Stack<Map<Integer, Integer>> st = new Stack<>();
        for(int i=arr.length-1; i>=0; i--) {
            while(!st.isEmpty() && st.peek().entrySet().stream().map(e->e.getValue()).findFirst().orElse(-1)>=arr[i]) {
                st.pop();
            }
            if(st.isEmpty()) {
                rs.add(arr.length);
            }else {
                rs.add(st.peek().entrySet().stream().map(e->e.getKey()).findFirst().orElse(-1)); 
            }
            Map<Integer, Integer> map = new HashMap<>();
            map.put(i, arr[i]);
            st.push(map); 
        }
        Collections.reverse(rs);

        return rs;

    }

    private static List<Integer> nearestSmallerLeft(int[] arr) {
        List<Integer> rs = new ArrayList<>();
        Stack<Map<Integer, Integer>> st = new Stack<>();
        for(int i=0; i<arr.length; i++) {
            while(!st.isEmpty() && st.peek().entrySet().stream().map(e->e.getValue()).findFirst().orElse(-1)>=arr[i]) {
                st.pop();
            }
            if(st.isEmpty()) {
                rs.add(-1);
            }else {
                rs.add(st.peek().entrySet().stream().map(e->e.getKey()).findFirst().orElse(-1)); 
            }
            Map<Integer, Integer> map = new HashMap<>();
            map.put(i, arr[i]);
            st.push(map); 
        }
        return rs;

    }
}
