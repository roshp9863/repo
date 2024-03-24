package com.inn.dsa.practice;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
class Tree {
    public static void main(String[] args) {
        TreeMap<Integer,Character> t = new TreeMap<>();
        t.put(1,'a');
        t.put(3,'b');
        t.put(2,'e');
        t.put(4,'d');

        System.err.println(t);
        // TreeMap<Integer,Character> t1 = t.stream().sort(comparator((a,b) -> b-a)).collect(Collectors.toMap());
        // System.err.println(t1);

//         List<City> listOfCities = new ArrayList<>;
// City sc = new City();
   
//    sc.setName("Nagpur");
//    sc.stateName("Maharashtra");
//    City sc1 = new City();
//    sc1.setName("Kanpur");
//    sc1.stateName("UP");
// Faizan Ali
// 3:34 PM
// Map<StateName, List<cityList>> peopleByAge
// Map<StateName, List<cityList>>

//Map<StateName, List<cityList>> =   listOfCities.stream().filter(c-> c.getName.contains("pur"))
//    .collect(Collectors.groupingBy(City::stateName, Function.identity()));
    }

    
}