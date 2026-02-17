package com.inn.functionInterface.practice;

@FunctionalInterface
public interface MyFunctionalInterface {
	
	void print(String msg);
	
	default void m1() {
		System.out.println("DEFAULT MSG");
	}
	
	static void m2() {
		System.out.println("STATIC METHOD");
	}
}
