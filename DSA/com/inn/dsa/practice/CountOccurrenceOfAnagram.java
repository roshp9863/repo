package com.inn.dsa.practice;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CountOccurrenceOfAnagram {
    public static void main(String[] args) {
        String str = "forxxorfxdofr";
        String f = "for";
        int result = countOccurrenceOfAnagram(str, f);
        System.out.println("Number of occurrences: " + result);
    }

    public static int countOccurrenceOfAnagram(String str, String f) {
        Map<Character, Long> fMap = Arrays.stream(f.split(""))
                .collect(Collectors.groupingBy(w -> w.charAt(0), Collectors.counting()));
        
        int k = f.length();
        int result = 0;
        int i = 0;
        int j = 0;

        while (j < str.length()) {
            if (j - i + 1 < k) {
                j++;
            } else if (j - i + 1 == k) {
                Map<Character, Long> windowMap = new HashMap<>();
                for (int x = i; x <= j; x++) {
                    char c = str.charAt(x);
                    windowMap.put(c, windowMap.getOrDefault(c, 0L) + 1);
                }
                if (windowMap.equals(fMap)) {
                    result++;
                }
                char leftChar = str.charAt(i);
                windowMap.put(leftChar, windowMap.get(leftChar) - 1);
                i++;
                j++;
            }
        }
        return result;
    }
}
