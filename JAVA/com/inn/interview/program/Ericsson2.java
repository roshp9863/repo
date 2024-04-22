package com.inn.interview.program;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Ericsson2 {
	
	public static void main(String[] args) {
//		THREAD-1 : 1
//		THREAD-2 : 2
//		THREAD-1 : 3
//		THREAD-2 : 4
		
		List<Integer> list = new ArrayList<>();
		for(int i=1; i<=10; i++) {
			list.add(i);
		}
		
//		Thread t1 = new Thread();
//		t1.start();
//		
//		
//		
//		t1.run();
//		m1(list,t1);
		
		add(4,5);
		add(4,5,6);
		
		//overriding
		
//		@Override toString()
		
		//Map
		//ArrayList // 
		//LinkedList //{}
		//ConcurrentHashMap
		//Set
		
				
		List<Integer> newList = list.stream().filter(i-> i%2==0).collect(Collectors.toList());
//		System.out.println(newList);
		
		
		
//		Find number of bits set in the given integer number:
//			10 = 1010 => 2
//			15 = 1111 => 4
		
		int num = 10;
		List<Integer> bits = new ArrayList<>();
		
		while(num>0) {
			int remain = num%2;
			bits.add(remain);
			num = num/2;
			
		}
		int count = (int) bits.stream().filter(f -> f==1).count();
		System.out.println(count);
		
		
//		Find two non repeating element from this array
		int[] arrs = {1,2,1,2,3,4,5,5};
		
		
		int x = arrs[0];
		for(int i=1; i<arrs.length; i++) {
			x = x^arrs[i];
		}
		System.out.println("X ----> {}" + x);
		
		
//		Find longest palindrome substring
//		i: malayalamabmalayalamabccba --> malayalam
//		i: malayalamabmalayalamabcdefggfedcba --> abcdefggfedcba
		
//		List of duplicate characters in String 
//		'Programming' 
//		g : 2 
//		r : 2 
//		m : 2
		
		String str = "Programming";
		List<String> l = Arrays.asList(str.split(""));
		Map<String, Long> map = l.stream().collect(Collectors.groupingBy(w->w, Collectors.counting()));
		System.out.println(map);
		
		Map<String, Long> map2 =  map.entrySet().stream().filter(e->e.getValue()==2).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		System.out.println(map2);
		
		//Input: array = {12, 3, 4, 1, 6, 9}, sum = 24; 
		//Output: 12, 3, 9 //a+b+c=sum ==> a+b = sum-c;
		int[] arr3 = {1,2,3,4,5};
		int k=0;
		int sum =20;
		int j = arr3.length-1;
		boolean flag = false;
		for(int i=1; i<arr3.length; i++)
			while(k<j) {
				if(arr3[k]+arr3[j] == sum-arr3[i] && arr3[i]!= arr3[k] && arr3[j]!= arr3[k]) {
					System.out.println("i "+ arr3[i] +" j "+ arr3[j] + " k "+ arr3[k]);
					flag=true;
					break;
				}else if(arr3[k]+arr3[j] > sum-arr3[i])	{
					j--;
				}else {
					k++;
			}
			if(flag) {
				break;
			}
		}
		
		
	}

	private static void add(int i, int j, int k) {
		
	}

	private static void add(int i, int j) {
	}
	
//	public synchronized static void m1(List<Integer> list, Thread t1) {
//		for(int i: list) {
//			
//			System.out.println("thread : " + t1.getName() + " " + i);
//		}
//		
//	}
	

}
