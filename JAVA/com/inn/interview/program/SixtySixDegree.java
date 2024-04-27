package com.inn.interview.program;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SixtySixDegree {
	public static void main(String[] args) {
		
		
//		We want to have a voting system where we have list of candidates:
//
//			Given an array of names of candidates in an election. 
//			A candidate's name in the array represents a vote cast on the candidate. 
//			if candidates have equal scores, print them. Find the candidates with the highest votes. 
//
//
//			Examples: 
//			Input:  votes[] = ["john", "johnny", "jackie", "johnny", "john", "jackie", "jamie", "jamie", "john", "johnny", "jamie", "johnny", "john"] 
//			Output: john Johnny
//			Explanation: We have four Candidates with name as 'John', 'Johnny', 'jamie', 'jackie'. The candidates John and Johnny get maximum votes.
//
//			Input: votes[] = ["virat", "rohit", "rishabh", "rohit", "virat", "rohit"]
//			Output: rohit
//			Explanation: We have three Candidates with name as 'virat', 'rohit', 'rishabh'. The candidate Rohit get maximum votes. 
//
//
//			*/
//
//			class Candidates {
//			    public static void main(String args[]){
//
//			    }
//
//			}
		
		
		List<String> votelist = Arrays.asList("john", "johnny", "jackie", "johnny", "john", "jackie", "jamie", "jamie", "john", "johnny", "jamie", "johnny", "john");
		Map<String, Long> map = votelist.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
		
		
//		long maxValue = map.values().stream().mapToLong(Long::longValue).max().orElse(0);
//
//        map.entrySet().stream()
//            .filter(entry -> entry.getValue() == maxValue)
//            .map(Map.Entry::getKey)
//            .forEach(System.out::println);
//		System.out.println(map);
		
		
		//{john: 4}
		
		Long max = 0l;
		
		
		Long max2 = Collections.max(map.values());
//		Set<String> set = new HashSet<>();
//		for(Map.Entry<String, Long> m: map.entrySet()) {
//			if(m.getValue()>max) {
//				max = m.getValue();
//			}
//		}
		
		for(Map.Entry<String, Long> m: map.entrySet()) {
			if(m.getValue()==max2) {
				System.out.println("NAMES ---> "+ m.getKey());
			}
		}
		
		//{john: 4}
		
	}
}
