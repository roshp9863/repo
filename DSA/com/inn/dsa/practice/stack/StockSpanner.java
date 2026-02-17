package com.inn.dsa.practice.stack;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class StockSpanner {
	//https://leetcode.com/problems/online-stock-span/ {medium}
	
    Stack<List<Integer>> priceSpan = new Stack<>();
    public StockSpanner() {
        
    }
    
    public int next(int price) {
        int span=1; 
        while(!priceSpan.isEmpty() && priceSpan.peek().get(0)<=price) {
        	span = span + priceSpan.peek().get(1);
        	priceSpan.pop();
        	
        }
        priceSpan.push(Arrays.asList(price,span));
        return span;
    }

	public static void main(String[] args) {
		StockSpanner stockSpanner = new StockSpanner();
		System.out.println(stockSpanner.next(100)); // return 1
		System.out.println(stockSpanner.next(80)); // return 1
		System.out.println(stockSpanner.next(60)); // return 1
		System.out.println(stockSpanner.next(70)); // return 1
		System.out.println(stockSpanner.next(60)); // return 1
		System.out.println(stockSpanner.next(75));  // return 4, because the last 4 prices (including today's price of 75) were less than or equal to today's price.
		System.out.println(stockSpanner.next(85));  // return 6
	}
	

}
