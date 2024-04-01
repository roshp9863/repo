package com.inn.functionInterface.practice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.function.Predicate;

interface Addable {
	int addition(int a, int b);
}

//class AddableImpl implements Addable {
//
//	@Override
//	public int addition(int a, int b) {
//		return (a+b);
//	}
//	
//}

public class Operation {
	public static void main(String[] args) {
		Addable add = (a,b) -> (a+b);
		int result = add.addition(15, 15);
		System.out.println(result);
		
		
		List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(11);
        numbers.add(5);
        numbers.add(6);
        numbers.add(4);
        
        List<Integer> filteredList = numbers.stream()
            .filter(((Predicate<Integer>)(num) -> num > 5).and((num) -> num < 10)) // Filter numbers greater than 5 AND less than 10
            .collect(Collectors.toList());
        
        System.out.println(filteredList); // Output: [6]
     }
}
