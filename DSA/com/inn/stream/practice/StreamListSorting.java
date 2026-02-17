package com.inn.stream.practice;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamListSorting {
	public static void main(String[] args) {
		List<String> fruit = new ArrayList<>();
		fruit.add("apple");
		fruit.add("mango");
		fruit.add("banana");
		fruit.add("orange");
		
		List<String> sortedFruits = fruit.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
		System.out.println(sortedFruits);

		List<String> sortedFruits1 = fruit.stream().sorted((o1,o2) -> o1.compareTo(o2)).collect(Collectors.toList());
		System.out.println(sortedFruits1);

		List<String> sortedFruits2 = fruit.stream().sorted().collect(Collectors.toList());
		System.out.println(sortedFruits2);

		List<Integer> sortedFruitsLength2 = fruit.stream().map(s->s.length()).collect(Collectors.toList()).stream().sorted().collect(Collectors.toList());
		System.out.println(sortedFruitsLength2);

		
		//descending order
		
		List<String> sortedFruitsDesc = fruit.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		System.out.println(sortedFruitsDesc);

		List<String> sortedFruitsDesc1 = fruit.stream().sorted((o1,o2) -> o2.compareTo(o1)).collect(Collectors.toList());
		System.out.println(sortedFruitsDesc1);

		List<Employee> empList = new ArrayList<>();
		empList.add(new Employee(1, "rosh", 25, 4500000));
		empList.add(new Employee(2, "akku", 27, 5500000));
		empList.add(new Employee(3, "lali", 21, 6500000));
		empList.add(new Employee(4, "nancy", 22, 7500000));
		empList.add(new Employee(5, "kalyani", 22, 8500000));
		empList.add(new Employee(6, "kanu", 19, 9500000));
		
		List<Employee> employeeSortedList = empList.stream().sorted(new Comparator<Employee>() {
			@Override
			public int compare(Employee o1, Employee o2) {
				return (int) (o1.getAge() - o2.getAge());
			}
		}).collect(Collectors.toList());

		List<Employee> employeeSortedList2 = empList.stream().sorted((o1, o2) -> (int) (o1.getAge() - o2.getAge())).collect(Collectors.toList());
		System.out.println(employeeSortedList2);
		
		List<Employee> employeeSortedList3 = empList.stream().sorted((o1, o2) -> o1.getName().compareTo(o2.getName())).collect(Collectors.toList());
		System.out.println(employeeSortedList3);
		
		List<Employee> employeeSortedList4 = empList.stream().sorted(Comparator.comparingLong(Employee::getSalary)).collect(Collectors.toList());
		System.out.println(employeeSortedList4);
		
		List<Employee> employeeSortedList5 = empList.stream().sorted(Comparator.comparingInt(Employee::getAge).reversed()).collect(Collectors.toList());
		System.out.println(employeeSortedList5);

		List<Employee> employeeSortedList6 = empList.stream().sorted(Comparator.comparing(Employee::getName).reversed()).collect(Collectors.toList());
		System.out.println(employeeSortedList6);
		


	}
}
