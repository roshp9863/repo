package com.inn.stream.practice;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//https://www.youtube.com/watch?v=UfJ0lCH7FMM java guides Java 8 Tutorial | Java 8 Features in One Video | 4 Hours Full Course 🔥


public class CreateStreamObject {
	
	public static void main(String[] args) {
		
		//create a stream 
		Stream<String> stream = Stream.of("a", "b", "c");
		stream.forEach(System.out::println);
		
		//create a stream from Sources
		Collection<String> collections = Arrays.asList("Java", "springboot"); 
		Stream<String> stream2 = collections.stream();
		stream2.forEach(System.out::println);
		

		//create a stream from Sources
		List<String> list = Arrays.asList("Java", "springboot"); 
		Stream<String> stream3 = list.stream();
		stream3.forEach(System.out::println);

		//create stream using array
		String[] arr = {"a", "b", "c"};
		Stream<String> stream4 = Arrays.stream(arr);
		stream4.forEach(System.out::println);
		
		//create Stream using primitive array
		int[] a = {3, 4, 2, 1, 4, 3};
        Integer[] boxedArr = Arrays.stream(a).boxed().toArray(Integer[]::new);
		Stream<Integer> stream5 = Arrays.stream(boxedArr);
		stream5.forEach(System.out::println);


		
		
	}

}
