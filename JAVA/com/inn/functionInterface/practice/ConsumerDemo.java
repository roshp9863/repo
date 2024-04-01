package com.inn.functionInterface.practice;

import java.util.function.Consumer;


//class ConsumerImpl implements Consumer<String>{
//
//	@Override
//	public void accept(String t) {
//		System.out.println(t);
//	}
//
//}


public class ConsumerDemo {
	
	public static void main(String[] args) {
//		Consumer<String> fun = new ConsumerImpl();
//		fun.accept("ROSHANI");
		
		Consumer<String> fun = (String s) -> System.out.println(s);
		fun.accept("JASSU");
		
	}


}
