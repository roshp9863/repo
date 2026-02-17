package com.inn.company.interview;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ImpetusATM {


	    public static void main(String[] args) {
	    	
	    	
	    	System.out.println("Hello"+10+20+30);
	    	
	        Scanner scanner = new Scanner(System.in);
	        System.out.print("Enter the amount to be withdrawn: ");
	        int amountToWithdraw = scanner.nextInt();

	        int[] currencyDenominations = {2000, 500, 200, 100}; // Denominations in descending order

	        Map<Integer, Integer> notesCount = minimumNotes(amountToWithdraw, currencyDenominations);

	        System.out.println("Denomination\tCount");
	        for (Map.Entry<Integer, Integer> entry : notesCount.entrySet()) {
	            System.out.println(entry.getKey() + "\t\t\t" + entry.getValue());
	        }
	    }

	    public static Map<Integer, Integer> minimumNotes(int amount, int[] denominations) {
	        Map<Integer, Integer> notesCount = new HashMap<>();
	        int remainingAmount = amount;

	        for (int denomination : denominations) {
	            if (remainingAmount >= denomination) {
	                notesCount.put(denomination, remainingAmount / denomination);
	                remainingAmount %= denomination;
	            }
	        }
	        return notesCount;
	    }
	
	    
	    
	 // Functional Interface
	    @FunctionalInterface
	    interface Calculator {
	        int calculate(int a, int b);
	    }

	    // CalculatorService class using the functional interface
	    public class CalculatorService {
	        public static void main(String[] args) {
	            // Using lambda expression to implement the functional interface
	            Calculator addition = (a, b) -> a + b;
	            Calculator subtraction = (a, b) -> a - b;
	            Calculator multiplication = (a, b) -> a * b;
	            
	            // Using the functional interface to perform calculations
	            System.out.println("Addition result: " + performOperation(addition, 5, 3));
	            System.out.println("Subtraction result: " + performOperation(subtraction, 5, 3));
	            System.out.println("Multiplication result: " + performOperation(multiplication, 5, 3));
	        }

	        // Method to perform calculation using the functional interface
	        public static int performOperation(Calculator calculator, int a, int b) {
	            return calculator.calculate(a, b);
	        }
	    }


}
