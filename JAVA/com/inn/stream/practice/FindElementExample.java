package com.inn.stream.practice;

import java.util.*;

public class FindElementExample {
	public static void main(String[] args) {
		List<Integer> list = Arrays.asList(1,2,3,2,1);
		
		Optional<Integer> op = list.stream().findFirst();
		Integer rs = op.isPresent() ? op.get() : -1;
		System.out.println(rs);

		
		Optional<Integer> op1 = list.stream().findAny();
		Integer rs1 = op1.isPresent() ? op1.get() : -1;
		System.out.println(rs1);

	}

}
