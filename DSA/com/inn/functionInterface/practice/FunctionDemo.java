package com.inn.functionInterface.practice;

import java.util.function.Function;

//class FunctionImpl implements Function<String, Integer>{
//
//	@Override
//	public Integer apply(String t) {
//		return t.length();
//	}
//
//}

public class FunctionDemo {
	
	public static void main(String[] args) {
//		Function<String, Integer> fun = new FunctionImpl();
//		fun.apply("ROSHANI");
		
		Function<String, Integer> fun = (String s) -> s.length();
		System.out.println(fun.apply("JASSU"));
		
	}
	
}
