package com.inn.interview.program;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EPAM {
	public static void main(String[] args) {
		String str = "date,amount,name\n2024-02-12,300,product1\n2023-12-11,400,product1\n2024-01-01,5000,product2\n2024-02-22,2000";
		//output should be in map {product1:[300,400], product2:[2000]}
		List<String> list = Arrays.asList(str.split("\n"));
		
		System.out.println("LIST ---> "+ list);
		
		List<String> list1= list.stream().skip(1).collect(Collectors.toList());
		
		System.out.println("LIST1 ---> "+ list1);
		
	 }
	

	    // Check if a string represents a valid integer
	    private static boolean isAmount(String str) {
	        try {
	            Integer.parseInt(str);
	            return true;
	        } catch (NumberFormatException e) {
	            return false;
	        }
	    }
}
