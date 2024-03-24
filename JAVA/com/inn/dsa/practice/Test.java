package com.inn.dsa.practice;
import java.util.stream.Collectors;
import java.util.*;

class Test {
    public static void main(String[] args) {
        String[] names = {"Kartikey", "Suvansh", "Jaya", "jaya", "Singh", "samarth"};
        List<String> name = new ArrayList<>();
        name.add("Kartikey");
        name.add("Suvansh");
        name.add("Jaya");
        name.add("jaya");
        name.add("Singh");        
        name.add("samarth");
        List<String> namesWithJ = Arrays.asList(names).stream().filter(x-> x.charAt(0)=='j' || x.charAt(0)=='J').collect(Collectors.toList());
        System.out.println(namesWithJ);

        int[] num = {87, 56, 56, 73, 32, 73, 12, 13};

        int max = Integer.MIN_VALUE;
        int max2 = Integer.MIN_VALUE;
        int max3 = Integer.MIN_VALUE;
        for(int i=0; i<num.length; i++) {
            if(num[i]>max) {
                max = num[i];
            }
        }
        for(int i=0;i<num.length; i++) {
            if(num[i]>max2 && num[i]!=max) {
                max2=num[i];
            }

        }
        for(int i=0;i<num.length; i++) {
            if(num[i]>max3 && num[i]!=max && num[i]!=max2) {
                max3=num[i];
            }

        }

        System.out.println(max3);
    }
}