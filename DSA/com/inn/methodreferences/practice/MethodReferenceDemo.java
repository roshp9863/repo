package com.inn.methodreferences.practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
//https://www.youtube.com/watch?v=UfJ0lCH7FMM java guides Java 8 Tutorial | Java 8 Features in One Video | 4 Hours Full Course 🔥

@FunctionalInterface
interface Printable {
	void print(String msg);
}

public class MethodReferenceDemo {
	
	public void display(String msg) {
		msg = msg.toUpperCase();
		System.out.println(msg);
	}
	
	public static int add(int a, int b) {
		return (a+b);
	}
    
    public static void main(String[] args) {
    	
    	//1. method reference to a static method
    	//lambda expression
        Function<Integer, Double> fun = (input) -> Math.sqrt(input);
        System.out.println(fun.apply(4));
        
        //using method reference
        Function<Integer, Double> fun1 = Math::sqrt;
        System.out.println(fun1.apply(4));
        
      //using method reference
        BiFunction<Integer, Integer, Integer> biFun = MethodReferenceDemo::add;
        System.out.println(biFun.apply(2, 3));
        
        //2. Method reference to an instance method of an object
        MethodReferenceDemo md = new MethodReferenceDemo();
        
        //lambda expression
        Printable p = (msg) -> md.display(msg);
        p.print("msg");
        
        //using method reference
        Printable p1 = md::display;
        p1.print("abc");
        
        
        //3. Reference to the instance method of an arbitrary obj
        //Sometimes, we call a method of argument in the lambda expression.
        //In that case, we can use a method reference to call an instance
        //method of an arbitrary obj of a specific type
        
        Function<String, String> str = (input) -> input.toLowerCase();
        System.out.println(str.apply("JAVA GUIDES"));
        
        //using method references
        Function<String, String> str1 = String::toLowerCase;
        System.out.println(str1.apply("JAVA GUIDES METHOD REFERENCE"));
        
        String[] arr = {"a", "c", "e", "d", "f", "b"};
        
        Arrays.sort(arr, (a,b) -> a.compareToIgnoreCase(b));
        Arrays.sort(arr, String::compareToIgnoreCase);
        
        
        //4. Reference to a Constructor
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("mango");
        list.add("banana");
        list.add("orange");
        list.add("apple");
        
        System.out.println("LIST ---> "+ list);
        
        Function<List<String>, Set<String>> funList = (ip) -> new HashSet<>(ip);
        System.out.println("SET ----> " + funList.apply(list));
        
        //method reference
        
        Function<List<String>, Set<String>> funList1 = HashSet::new;
        System.out.println("SET1 ----> " + funList1.apply(list));
        
        
        
        
    }
}
