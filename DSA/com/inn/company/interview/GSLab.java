package com.inn.company.interview;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GSLab {
	public static void main(String[] args) {
		int num = 12345;
		int sum=0;

//		System.out.println(findSum(num, sum));
		
		int[] arr = {1,2,3,1,2,3,4,5,1};
		Integer[] arr1 = Arrays.stream(arr).boxed().toArray(Integer[]::new);
		Map<Integer, Long> map = Arrays.stream(arr1).collect(Collectors.groupingBy(w->w, Collectors.counting()));
		Map<Integer, Long> list2 = map.entrySet().stream().filter(e ->e.getValue()>1).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		System.out.println(list2);
		
		
	}

	private static int findSum(int num, int sum) {
		if(num<10) {
			return num;
		}
		while(num>0) {
			int n = num%10;
			sum = sum+n;
			num = num/10;
		}
		if(sum>10) {
			return findSum(sum, 0);
		}
		return sum;
		
	}
	
	//postmapping(/addStudent)
	//createStudent
	//id,name,dob,address,contactnumber
	
	//put(/)

}
