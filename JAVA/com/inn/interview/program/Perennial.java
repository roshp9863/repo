package com.inn.interview.program;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Perennial {
	public static void main(String[] args) {
		List<Integer> list = Arrays.asList(2,1,4,6);
		
		List<Integer> updatedList = list.stream().filter(a -> (a%2)==0).map(a->a*2).collect(Collectors.toList());
		
		System.out.println(updatedList);
		
//		Input -> "My name is ABC"
//		Output -> "ym eman si cba"
		
		String str = "My name is ABC";
		String newStr ="";
		
		List<String> strList = Arrays.asList(str.split(" "));
		
		for(int i=0; i<strList.size(); i++) {
			
			for(int j=strList.get(i).length()-1; j>=0; j--) {
				newStr = newStr + strList.get(i).toLowerCase().charAt(j);
			}
			newStr = newStr + " ";
		}
		
		System.out.println("Output ----> "+ newStr);
				

	}
}
