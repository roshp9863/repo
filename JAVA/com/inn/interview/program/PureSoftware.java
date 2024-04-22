package com.inn.interview.program;

import java.util.Arrays;
import java.util.List;

public class PureSoftware {

	public static void main(String[] args) {
		int[] arr = {2,3,1,4,6,7};
		int max = Integer.MIN_VALUE;
		int secondMax = Integer.MIN_VALUE;
		for(int i=0; i<arr.length; i++) {
			
			if(max<arr[i]) {
				max=arr[i];
			}
			
		}
		
		for(int i=0; i<arr.length; i++) {
			
			if(secondMax<arr[i] && arr[i]!=max) {
				secondMax=arr[i];
			}
			
		}

		
		
		System.out.println(secondMax);
		
		String str = "My name is Roshani";
		List<String> list = Arrays.asList(str.split(" "));
		String newStr = "";
		
		for(int i =0; i<list.size(); i++) {
			for(int j=list.get(i).length()-1; j>=0; j--) {
				newStr = newStr + list.get(i).charAt(j);
			}
			newStr = newStr + " ";
		}
		
		System.out.println(newStr);
		
//		for(int i=0; i<str.length(); i++) {
//			char a = str.charAt(i);
//			
//			
//		}
		
	}
}
