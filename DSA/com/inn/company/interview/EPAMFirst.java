package com.inn.company.interview;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EPAMFirst {
	public static void main(String[] args) {
		String data = "date,amount,name\n2024-02-12,300,product1\n2023-12-11,400,product1\n2024-01-01,5000,product2\n2024-02-22,2000,product2";		//output should be in map {product1:[300,400], product2:[2000]}
		Map<String, List<Long>> productAmounts = processData(data);

	    System.out.println(productAmounts);
	 }
	

	    // Check if a string represents a valid integer
	public static Map<String, List<Long>> processData(String data) {
	    return data.lines() 
	        .skip(1) 
	        .map(line -> line.split(",")) 
	        .map(values ->  
	                Map.entry(values[2], Long.parseLong(values[1])))  
	        .collect(Collectors.groupingBy(Map.Entry::getKey, 
	                Collectors.mapping(Map.Entry::getValue, Collectors.toList()))); 
	  }
}
