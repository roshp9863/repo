package com.inn.interview.program;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Ericsson {
	
	public static void main(String[] args) {
//		int[] arr = {3,4,2,1,4,3};
//		Map<Integer, Long> map = Arrays.asList(arr).stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//		System.out.println(map);
//		
//				map.entrySet().stream().filter(entry -> entry.getValue()>1).forEach(System.out::println);
		
		
//		Given two sorted arrays, the task is to merge them in a sorted manner.
//
//		Input: arr1[] = { 1, 3, 4, 5}, arr2[] = {2, 4, 6, 8} 
//		Output: arr3[] = {1, 2, 3, 4, 4, 5, 6, 8}
		
		
		int[] arr = {3, 4, 2, 1, 4, 3};
        Integer[] boxedArr = Arrays.stream(arr).boxed().toArray(Integer[]::new);

        Map<Integer, Long> map = Arrays.stream(boxedArr)
                                       .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println(map);

        map.entrySet().stream()
                      .filter(entry -> entry.getValue() > 1)
                      .forEach(System.out::println);
        
        
	int arr1[] = {1, 3, 4, 5};
	int arr2[] = {2, 4, 6, 8};
	int arr3[] = new int[arr1.length+arr2.length];
	
	int i =0 ;
	int j=0;
	int k=0;
	while(i<arr1.length && j<arr2.length) {
		if(arr1[i]<arr2[j]) {
			arr3[k] = arr1[i];
			i++;
			k++;
		}else {
			arr3[k] = arr2[j];
			j++;
			k++;
		}
	}
	while(j<arr2.length) {
		arr3[k] = arr2[j];
		j++;
		k++;
	}
	while(i<arr1.length) {
		arr3[k] = arr2[i];
		i++;
		k++;
	}
	
	System.out.println(Arrays.toString(arr3));

	}

}
