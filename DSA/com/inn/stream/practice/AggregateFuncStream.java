package com.inn.stream.practice;

import java.util.Comparator;
import java.util.stream.Stream;

public class AggregateFuncStream {
	//https://www.youtube.com/watch?v=UfJ0lCH7FMM java guides Java 8 Tutorial | Java 8 Features in One Video | 4 Hours Full Course 🔥

	//max,count, min
	public static void main(String[] args) {
		Stream<Integer> stream = Stream.of(1,2,3,4,5,6,7,8,9);
		System.out.println(stream.count());
		
		Integer min = Stream.of(1,2,3,4,5,6,7,8,9).min(Comparator.comparing(Integer::valueOf)).get();
		Integer min2 = Stream.of(1,2,3,4,5,6,7,8,9).reduce(Integer::min).orElse(null);
		Integer min3 = Stream.of(1,2,3,4,5,6,7,8,9).reduce(Integer.MAX_VALUE, Integer::min);
		
	}
}
