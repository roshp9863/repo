package com.inn.functionInterface.practice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class BiConsumerDemo {
	//https://www.youtube.com/watch?v=UfJ0lCH7FMM java guides Java 8 Tutorial | Java 8 Features in One Video | 4 Hours Full Course 🔥
	public static void main(String[] args) {
		BiConsumer<Integer, Integer> add = (t,u) -> System.out.println(t + u);
		BiConsumer<Integer, Integer> sub = (t,u) -> System.out.println(t - u);
		BiConsumer<Integer, Integer> mul = (t,u) -> System.out.println(t * u);
		BiConsumer<Integer, Integer> div = (t,u) -> System.out.println(t / u);
		add.accept(200, 100);
		sub.accept(200, 100);
		mul.accept(200, 100);
		div.accept(200, 100);
		
		Map<Integer, String> map = new LinkedHashMap<>();
		map.put(1, "rosh");
		map.put(2, "akku");
		map.put(3, "lali");
		map.put(4, "nancy");
		
		map.forEach((k,v) -> {
			System.out.println(k); 
			System.out.println(v);
		});
		
	}
}
