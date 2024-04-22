//package com.inn.interview.program;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class PureSoftware2 {
//	public static void main(String[] args) {
//		List<Integer> list = Arrays.asList(1,2,3,4,5,6);
//		
////		List<Integer> newList = list.stream().filter(a->a%2==0).collect(Collectors.toList());
////		
////		System.out.println(newList);
//		
//		String s1 = "laxcptynlu";
//		String s2 = "aptnl";
//		String s3 = "apptnl";
//		
//		int j=0;
//		for(int i=0; i<s1.length(); i++) {
//			while(j<s2.length()) {
//				if(s1.charAt(i)==s2.charAt(j)) {
//					j++;
//				}else {
//					continue;
//				}
//			}
//		}
//		
//		
//		if(j+1==s2.length()) {
//			System.out.println("TRUE");
//		}
//		
//		employee
//		id, name
//		
//		employeedetails
//		empid, address, departmentid
//		
//		department
//		id, departmentname
//		
//		
//		
//		select e.id, e.name, address, departmentname from employee e inner join employeedetails e1 on e.id=empid 
//		inner join department d on d.id=e1.departmentid where name='' or empid='' or department
//	}
//}
