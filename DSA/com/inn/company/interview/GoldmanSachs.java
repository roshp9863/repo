package com.inn.company.interview;

import java.util.*;
import java.util.stream.Collectors;

public class GoldmanSachs {

	public static String collapseString(String inputString) {

		//GGGTTGGGG
		//GGGGGrrrrrrrrrrrrrrt
		//AABBCC
		//ABC
		//AAAA
		
		
		System.out.println("S --> " + inputString);
		// if(inputString.isBlank() || inputString.isEmpty()) {
		// return new String();
		// }
		// int count=1;
		// int lastcount = 1;
		// StringBuilder s = new StringBuilder();
		// for(int i=0; i<inputString.length()-1; i++) {
		// if(inputString.charAt(i)==inputString.charAt(i+1)) {
		// count++;
		// }else {
		// s = s.append(count).append(inputString.charAt(i));
		// lastcount=count;
		// count=1;
		// }
		// }

		// if(inputString.length()>2 &&
		// inputString.charAt(inputString.length()-1)==inputString.charAt(inputString.length()-2))
		// {
		// char c = (char)((lastcount++) + '0');
		// s.setCharAt(s.length()-2, c);
		// }else {
		// s.append(1).append(inputString.charAt(inputString.length()-1));
		// }
		// System.out.println("myop -----> "+ s);

		List<String> list = Arrays.asList(inputString.split(""));
		LinkedHashMap<String, Long> map = list.stream()
				.collect(Collectors.groupingBy(w -> w, LinkedHashMap::new, Collectors.counting()));
		String str = map.entrySet().stream().map(entry -> entry.getValue() + entry.getKey())
				.collect(Collectors.joining());
		return str;
	}
	
	public static List<String> getShrunkArray(List<String> inputArray, int burstLength) {
 
		
//		input [a, b, c, d, e, e, e, e, e, d, d, c, b, f, g, f, h, h, h, t]
//		burstLength 3
//		o/p - [a,b,c,c,b,f,g,f,t]
		
//		input [a, b, c, b]
//		burstLength 2
//				 
//	    input [a, b, c, d]
//		burstLength 3
//		 
//		input [a, b, c, c, c, d, e, e, f]
//		burstLength 3
//		
//		input [a, b, e, e, e, e, e, b, b, b, b, d, d, f, f, f, f, f, d, d, d, k, l, m, m, m, m, m, t, s]
//		burstLength 5
//		  
//		input [a, k]
//		burstLength 3
//		
//		input [a, a]
//		burstLength 2
//		
//		input [a, b]
//		burstLength 1
		
		
		System.out.println(" input "+ inputArray);
        System.out.println(" burstLength "+ burstLength);
        
        List<String> s = new ArrayList<>();
        LinkedHashMap<String, Long> map = inputArray.stream().collect(Collectors.groupingBy(w->w, LinkedHashMap::new, Collectors.counting()));
    System.out.println(map);
    for(Map.Entry<String, Long> entry: map.entrySet()) {
        String key = entry.getKey();
        Long value = entry.getValue();
        
        if(value<burstLength) {
            for(int i=0; i<value; i++) {
                s.add(key);
            }
        }
        
    }
       return s;    

    }

}
