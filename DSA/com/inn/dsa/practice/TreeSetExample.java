package com.inn.dsa.practice;

import java.util.TreeSet;

public class TreeSetExample {
    public static void main(String[] args) {
        // Using TreeSet to store employees (no duplicates)

    	TreeSet<Employee> treeSet = new TreeSet<>();
        treeSet.add(new Employee(101, "John"));
        treeSet.add(new Employee(103, "Alice"));
        treeSet.add(new Employee(102, "Bob"));
        treeSet.add(new Employee(101, "John")); // Duplicate employee ID

        System.out.println("Employees in TreeSet: ");
        for (Employee employee : treeSet) {
            System.out.println(employee);
        }
    }
}


class Employee implements Comparable<Employee> {
    private int id;
    private Integer salary;
    private String name;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int compareTo(Employee other) {
        // Compare employees based on their IDs
        return Integer.compare(this.id, other.id);
    }
    
    //FOR NAME
    
//    @Override
//    public int compareTo(Employee other) {
//        // Compare employees based on their names
//        return this.name.compareTo(other.name);
//    }

    
//  @Override
//  public int compareTo(Employee other) {
//      // Compare employees based on their names
//      return this.salary.compareTo(other.salary);
//  }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return id == employee.id;
    }

    //For name equals method
    
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//        Employee employee = (Employee) obj;
//        return name.equals(employee.name);
//    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
    
    //For name hashcode method

//    @Override
//    public int hashCode() {
//        return name.hashCode();
//    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}