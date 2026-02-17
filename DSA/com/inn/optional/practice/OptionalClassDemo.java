package com.inn.optional.practice;

import java.util.Optional;
//https://www.youtube.com/watch?v=UfJ0lCH7FMM java guides Java 8 Tutorial | Java 8 Features in One Video | 4 Hours Full Course 🔥


public class OptionalClassDemo {
	public static void main(String[] args) {
		
		String email = "rosh@gmail.com";
		
		Optional<Object> empty = Optional.empty();
		System.out.println(empty);
		
		Optional<String> emailOp = Optional.of(email); //of provide null check if str is null then it will throw nullpointerexec
		System.out.println(emailOp);
		
		Optional<String> emailNull = Optional.ofNullable(email); // so it will not throw nullpointerexec it will return optional empty
		System.out.println(emailNull);
		
		// to retrive value.. we just use get()
		System.out.println(emailNull.get()); // if we use get() and if the value is null then it will throw nosuchelementexception

		// so before get() we need to check where the value is present or not for this.. we use isPresent method
		
		if(emailNull.isPresent()) {
			System.out.println(emailNull.get());
		}else {
			System.out.println("NO VALUE PRESENT");
		}
		
		//orElse is used when we want some defaultvalue if the value is not present and it is always compute (eagerly)
		
		String defaultOptional = emailNull.orElse("default@gmail.com");
		
		//orElseGet it is lazy.. when the the optional is empty then only it will compute..
		String defaultOptional2 = emailNull.orElseGet(()->"default@gmail.com"); // it is using supplier internally it will return something
		
		//orElseThrow method throw the exception
		String defaultOptional3 = emailNull.orElseThrow(()-> new IllegalArgumentException("Email is not exist"));

		
		Optional<String> gender = Optional.of("FEMALE");
		Optional<Object> emptyOp = Optional.empty();
		
		gender.ifPresent((s) -> System.out.println("value is present " + s)); // it is using consumer internally it will take something
		emptyOp.ifPresent((s) -> System.out.println("value is not present")); 
		//nothing is present if value is not there nit even the sysout statement if we invoke ifPresent() by empty Optional 

		
		//filter method

		String result ="str";
		if(result!=null && result.contains("str")) {
			System.out.println(result);
		}
		
		Optional<String> op = Optional.of(result);
		op.filter(r -> r.contains("str")).ifPresent(res -> System.out.println(res));
		
		
		//map method
		op.filter(r -> r.contains("str")).map(String::toUpperCase).ifPresent(res -> System.out.println(res));
		
		
		
	}
}
