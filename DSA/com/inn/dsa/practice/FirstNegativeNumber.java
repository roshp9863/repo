package com.inn.dsa.practice;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

class FirstNegativeNumber {
    public static void main(String[] args) {
        int[] arr = {12,-1,-7,8,-15,30,18,28};
        int k=3;
        int i=0;
        Queue<Integer> q = new LinkedList<>();
        int j=0;

        while (j < arr.length) {
            if (j - i + 1 < k) {
                if (arr[j] < 0) {
                    q.offer(arr[j]); // 
                }
                j++;
                System.out.println("Q--> "+ q);
            } else if (j - i + 1 == k) {
            	
                if (arr[j] < 0) {
                    q.offer(arr[j]);
                }
                System.out.println("Q in else if --> "+ q + " j ---> "+ j);

                if (q.isEmpty()) {
                    System.out.print(0 + " ");
                } else {
                    System.out.print(q.peek() + " ");
                    if (arr[i] == q.peek()) {
                        q.poll();
                    }
                }
                i++;
                j++;
            }
        }       
         
    }
}