package com.inn.dsa.practice;
import java.util.*;
class Test1 {
    // Write a program to find the next big element in a give array and explain time complexity
    // Input : {2,6,3,4,9,1,10};
    // Output : [6,9,4,9,10,10,-1]
    
    public static void main(String[] args) {
        int[] arr = {2,6,3,4,9,1,10};
        List<Integer> list = new ArrayList<>();
        int i=1;
        int index=0;

        while(index<arr.length && i<arr.length) {
            if(arr[index]<arr[i]) {
                list.add(arr[i]);
                index++;
                i=index+1;
            }else {
                i++;
            }
        }
        list.add(-1);
        System.out.println(list);
    }

    // public static void main(String[] args) {
	// 	int[] arr = { 2, 6, 3, 4, 9, 1, 10 };
	// 	List<Integer> list = new ArrayList<>();
	// 	int i = 1;
	// 	int index = 0;

	// 	while (index < arr.length && i < arr.length) {
	// 		System.out.println("index == "+ index +" i == "+i);
	// 		if (arr[index] < arr[i]) {
	// 			list.add(arr[i]);
	// 			index++;
	// 			i=index+1;
	// 		} else {
	// 			i++;
	// 		}
	// 	}
	// 	list.add(-1);
	// 	System.out.println(list);
	// }
}