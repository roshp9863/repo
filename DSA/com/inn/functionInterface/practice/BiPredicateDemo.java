package com.inn.functionInterface.practice;

import java.util.function.BiPredicate;
import java.util.function.Function;

public class BiPredicateDemo {
	//https://www.youtube.com/watch?v=UfJ0lCH7FMM java guides Java 8 Tutorial | Java 8 Features in One Video | 4 Hours Full Course 🔥
	public static void main(String[] args) {
		BiPredicate<Integer, Integer> great = (t,u) -> t>u;
		BiPredicate<Integer, Integer> less = (t,u) -> t<u;
		BiPredicate<String, String> eq = (t,u) -> t.equals(u);
		
		System.out.println(great.test(10, 20));
		System.out.println(less.test(10, 20));
		System.out.println(eq.test("rosh", "rosh"));
	}
}
