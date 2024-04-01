package com.inn.functionInterface.practice;

import java.util.function.BiFunction;
import java.util.function.Function;

public class BiFunctionDemo {
	//https://www.youtube.com/watch?v=UfJ0lCH7FMM java guides Java 8 Tutorial | Java 8 Features in One Video | 4 Hours Full Course 🔥
	public static void main(String[] args) {
		BiFunction<Integer, Integer, Integer> add = (t,u) -> (t + u);
		BiFunction<Integer, Integer, Integer> sub = (t,u) -> (t - u);
		BiFunction<Integer, Integer, Integer> mul = (t,u) -> (t * u);
		BiFunction<Integer, Integer, Integer> div = (t,u) -> (t / u);
		Function<Integer, Integer> fun = (a) -> a * a;
		Integer rs = add.andThen(fun).apply(10, 20);
		
		System.out.println(add.apply(10, 20));
		System.out.println(sub.apply(200, 100));
		System.out.println(mul.apply(200, 100));
		System.out.println(div.apply(200, 100));
		
		System.out.println(rs);
	}
}
