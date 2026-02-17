package com.inn.functionInterface.practice;

@FunctionalInterface
interface Filter {
    boolean apply(int a);
}

@FunctionalInterface
interface FilterTwoParams {
    boolean apply(int a, int b);
}

class FilterImpl {
    Filter f;
    FilterTwoParams f2;

    FilterImpl(Filter f) {
        this.f = f;
    }

    FilterImpl(FilterTwoParams f2) {
        this.f2 = f2;
    }
}

public class StreamFilter {
    FilterImpl f = new FilterImpl((a) -> a % 2 == 0);
    FilterImpl f2 = new FilterImpl((a,b) -> (a + b == 10));

    public void printFilteredResult(int number) {
        boolean result = f.f.apply(number);
        if (result) {
            System.out.println("Number is even.");
        } else {
            System.out.println("Number is not even.");
        }
    }

    public void printFilteredResult(int number1, int number2) {
        boolean result = f2.f2.apply(number1, number2);
        if (result) {
            System.out.println("Sum of numbers is 10.");
        } else {
            System.out.println("Sum of numbers is not 10.");
        }
    }

    public static void main(String[] args) {
        StreamFilter sf = new StreamFilter();
        sf.printFilteredResult(2); // Output: Number is even.
        sf.printFilteredResult(3); // Output: Number is not even.
        sf.printFilteredResult(3, 7); // Output: Sum of numbers is 10.
        sf.printFilteredResult(4, 6); // Output: Sum of numbers is 10.
    }
}
