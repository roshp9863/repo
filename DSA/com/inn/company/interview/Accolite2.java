package com.inn.company.interview;

import java.util.HashSet;

import java.util.Arrays;

//class MySingleton {
//    private static MySingleton instance = null;
//
//    private MySingleton() {
//        // Private constructor to prevent instantiation from outside the class
//    }
//
//    public static synchronized MySingleton getInstance() {
//        if (instance == null) {
//            instance = new MySingleton();
//        }
//        return instance;
//    }
//}


//Interface parent {
//	Void display();
//}
//class Base implement parent
//{  
//void display()  
//{  
//  System.out.println("Inside the display method of base class.");    
//}  
//}  
//   
//class Derived  implement parent
//{  
//void display()  
//{  
//  System.out.println("Inside the display method of derived class.");    
//}  
//} 
//
//
//main() {
//	Derive d = new Base();
//	
//
//
//}

import java.util.HashMap;
public class Accolite2 {
    public static int countPairsWithSum(int[] arr, int K) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int count = 0;

        // Store frequency of each element in the array
        for (int num : arr) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        // Check for pairs
        for (int num : arr) {
            int complement = K - num;
            if (map.containsKey(complement)) {
                count += map.get(complement);
            }
            // If the current element is the complement itself, decrement count
            if (complement == num) {
                count--;
            }
        }

        // Divide by 2 because each pair is counted twice
        return count / 2;
    }

    public static void main(String[] args) {
        int[] arr = {10, 12, 10, 15, -1, 7, 6, 5, 4, 2, 1, 1, 1};
        int K = 11;

        System.out.println("Count of pairs with sum " + K + ": " + countPairsWithSum(arr, K));
        
        HashSet<Student> st = new HashSet<>();
        st.add(new Student(1, "xyz"));
        st.add(new Student(2, "xyz2"));
        st.add(new Student(1, "xyz")); // This should not be added to the set because it's a duplicate based on id
        System.out.println(st.size()); // Output should be 2

    }
}


class Accolite1 {
	public static void main(String[] args) {
		

		
		String[] a = {"xzy", "abcd", "ef", "jkl"};

        int maxLength = Arrays.stream(a)
                .mapToInt(String::length) // Convert strings to their lengths
                .max() // Find the maximum length
                .orElse(0); // If array is empty, return 0

        System.out.println("Maximum length of strings in the array: " + maxLength);
		

        








































//Class Library
//{
//Static {
//sout(“static parent”)
//}
//{
//sout(“ parent”)
//
//}
//
//Library()
//{
//sout(“parent const”)
//}
//
//
//
//}
//
//Class Student  extends Library
//{
//Static {
//sout(“static child”) 
//}
//
//{
//sout(“ child”)
//
//}
//
//Student ()
//{
//sout(“ child const”)
//}
//
//}
//
//Class Main {
//psvm(String arg[])
//{
//Student st = new Student();
//}
//
//
//Class Student {
//Int id;
//String name;
//Student (int id,String name){
//This .id =id;
//this.name =name;
//}
//
//@override
//Public int hashcode() {
//	Return method(id);
//}
//
//@override
//Public void equals(id) {
//	Return this.id.equals(id);
//}

}





//Class Main {
//psvm(String arg[])
//{
//HashSet<Student>st =new HashSet();
//st.add(new Student(1,”xyz”))
//st.add(new Student(2,”xyz2”))
//st.add(new Student(1,”xyz”))
//sout(st.size()); //2
//
//
//}
//
//ExcecutorService e = getThreads(2);
//
//Runnable i = { 
//	run();
//	
//}
//
//e.execute(i);
//e.close();
//
//Class Student implements Runnable {
//	
//	Id;
//Name;
//
//	Thread t1 = new Runnable();
//	Thread t2 = new Runnable();
//
//	Void display(){
//		t1.start()
//		t1.run()
//		sysout(“something”);
//}
//
//Void display2(){
//		t2.start()
//		t2.run()
//		sysout(“something2 ”);
//}
//
//	
//	
//
//}








//  4
///	\
//5    2
//    /  \
//   3    1
// /  \
//6   7
/// |
//10 20
//
//Right view of the tree :- 4,2,1,7
//
//display(root) {
//	  
//  if (root.right == None) {
//	return 
//  }
//
//  else
//  {syout(root.right.val) }
//	
//	display(root.right)
//	display(root.left)
//}
//syout(root.val);
//display(root);





//Array a = {“xzy”,”abcd”,”ef”,”jkl”}
//List<Integer> lengths = Arrays.stream(a).mapToInt(a->a.length).collect(Collector.toList());






//Input: arr[] = {10, 12, 10, 15, -1, 7, 6, 5, 4, 2, 1, 1, 1}, K = 11
//Output:  9
//
//Arrays.sort(arr);
////-1,1,1,1,2,4,5,6,7,10,10,12,15
//Int i=0;
//Int j=arr.length;
//Int count=0;
//while(i<j) {
//	if(arr[i]+arr[j]==k) {
//		I++;
//	J–;
//	count++;
//}else if(arr[i]+arr[j]>k) {
//	j–;
//}else {
//	i++;
//}
//}




		
	}
//}




class Student {
    int id;
    String name;

    Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Student student = (Student) obj;
        return id == student.id;
    }
}

class Main {
    public static void main(String[] args) {
        HashSet<Student> st = new HashSet<>();
        st.add(new Student(1, "xyz"));
        st.add(new Student(2, "xyz2"));
        st.add(new Student(1, "xyz")); // This should not be added to the set because it's a duplicate based on id
        System.out.println(st.size()); // Output should be 2
    }
}
