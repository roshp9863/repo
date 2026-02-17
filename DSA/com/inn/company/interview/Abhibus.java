package com.inn.company.interview;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Abhibus {

    public static void main(String[] args) {
        String str = "Java is Programming Java is Fun";

        List<String> list = Arrays.asList(str.split(" "));

        LinkedHashMap<String, Long> map = list.stream().
        collect(Collectors.groupingBy(w->w, LinkedHashMap::new, Collectors.counting())); 

        String firstNonRepeating = map.entrySet().stream()
        .filter(e -> e.getValue() == 1)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);

        System.out.println(firstNonRepeating);


    }




}

