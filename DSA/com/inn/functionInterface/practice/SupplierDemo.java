package com.inn.functionInterface.practice;

import java.time.LocalDateTime;
import java.util.function.Supplier;


//class SupplierImpl implements Supplier<LocalDateTime>{
//
//	@Override
//	public LocalDateTime get() {
//		return LocalDateTime.now();
//	}
//
//}


public class SupplierDemo {
	
	public static void main(String[] args) {
//		Supplier<String> fun = new SupplierImpl();
//		fun.get();
		
		Supplier<LocalDateTime> fun = () -> LocalDateTime.now();
		System.out.println(fun.get());
		
	}


}
