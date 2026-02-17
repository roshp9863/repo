package com.inn.company.interview;
import java.util.HashSet;
import java.util.Set;

public class EPAM2 {
 //List<Student> in student : name, List<Subject> in Subject name, score

	
	// {harry : 89} //average score or every student
	
//	students.stream()
//    .forEach(student -> {
//        double averageScore = student.subjects.stream()
//            .mapToInt(subject -> subject.score)
//            .average()
//            .orElse(0);
//        System.out.println(student.name + ": " + averageScore);
//    });
	

	
	//name of student who score more than 90
	
//	students.stream()
//    .filter(student -> student.subjects.stream().allMatch(subject -> subject.score > 90))
//    .map(student -> student.name)
//    .forEach(name -> System.out.print(name + " "));
	
	
	
	//major subject konsa liye popular subject 
	
	
//	students.stream()
//    .flatMap(student -> student.subjects.stream())
//    .collect(Collectors.groupingBy(subject -> subject.name, Collectors.counting()))
//    .entrySet().stream()
//    .max(Map.Entry.comparingByValue())
//    .ifPresent(entry -> System.out.println(entry.getKey()));

}

//{1,6,3,4,2,1} find out the minimum positive number here o/p is 5
//{1,2,3} // o/p is 4
//{-1,-3} o/p is 1


class Main2 {
    public static int findMinimumPositive(int[] nums) {
        Set<Integer> positiveNumbers = new HashSet<>();

        // Find all positive numbers in the array and add them to the set
        for (int num : nums) {
            if (num > 0) {
                positiveNumbers.add(num);
            }
        }

        // Check for the minimum positive number starting from 1
        int minPositive = 1;
        while (positiveNumbers.contains(minPositive)) {
            minPositive++;
        }

        return minPositive;
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 6, 3, 4, 2, 1};
        int[] nums2 = {1, 2, 3};
        int[] nums3 = {-1, -3};

        System.out.println("Minimum positive number for nums1: " + findMinimumPositive(nums1)); // Output: 5
        System.out.println("Minimum positive number for nums2: " + findMinimumPositive(nums2)); // Output: 4
        System.out.println("Minimum positive number for nums3: " + findMinimumPositive(nums3)); // Output: 1
    }
}
