package com.inn.interview.program;

import java.util.*;

public class Addverb {
	
	//Input: tasks = ["A","A","A","B","B","B"], n = 2
	//Output: 8
	
	
//	You are given an array of CPU tasks, each represented by letters A to Z, and a cooling time, n. Each cycle or interval allows the completion of one task. Tasks can be completed in any order, but there's a constraint: identical tasks must be separated by at least n intervals due to cooling time.
//	Return the minimum number of intervals required to complete all tasks.
//	 
//	Example 1:
//	Input: tasks = ["A","A","A","B","B","B"], n = 2
//	Output: 8
//	Explanation: A possible sequence is: A -> B -> idle -> A -> B -> idle -> A -> B.
//	After completing task A, you must wait two cycles before doing A again. The same applies to task B. In the 3rd interval, neither A nor B can be done, so you idle. By the 4th cycle, you can do A again as 2 intervals have passed.
//	Example 2:
//	Input: tasks = ["A","C","A","B","D","B"], n = 1
//	Output: 6
//	Explanation: A possible sequence is: A -> B -> C -> D -> A -> B.
//	With a cooling interval of 1, you can repeat a task after just one other task.

	
	//[1,2,3,4,5], [6]
	public static void main(String[] args) {
		String arr[] = {"A","A","A","B","B","B"};
		List<String> list = new ArrayList<>();
		
		int n = 2;
		int i=0;
		int j=0;
		int count=1;
		int k=1;
		
		while(i<arr.length && j<arr.length) {
			
			if((i+1<arr.length) && arr[i].equals(arr[i+1])) {
				i++;
				k=0;
			}else {
				list.add(arr[j]);
				j++;
				i=j+1;
				if(k<=n) {
					count++;
					k++;
				}	
			}
			
		}
		
		System.out.println(count);
		
	}

}
