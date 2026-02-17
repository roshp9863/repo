package com.inn.stream.practice;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GroupingByPractice {

    // Expected Output:
    // {
    // a=2,
    // b=2,
    // c=3
    // }

    public static void main(String[] args) {
        List<String> words = List.of("apple", "ant", "banana", "ball", "cat", "can", "cow");

        Map<Character, Long> map = words.stream()
                .collect(Collectors.groupingBy(a -> a.charAt(0), Collectors.counting()));

        List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eva");
        Map<Integer, List<String>> groupedLower = names.stream()
                .collect(Collectors.groupingBy(
                        name -> name.length(),
                        Collectors.mapping(String::toLowerCase, Collectors.toList())));

        List<Integer> nums = List.of(1, 5, 8, 12, 14, 17);

        Map<Integer, Optional<Integer>> maxByMod = nums.stream()
                .collect(Collectors.groupingBy(
                        n -> n % 4,
                        Collectors.maxBy(Comparator.naturalOrder())));

        List<String> fruits = List.of("apple", "banana", "kiwi", "orange", "fig");

        Map<Integer, String> joinedByVowelCount = fruits.stream()
                .collect(Collectors.groupingBy(
                        GroupingByPractice::countVowels,
                        Collectors.mapping(f -> f, Collectors.joining(","))));

        List<String> names2 = List.of("Alice", "Adam", "Bob", "Bruno", "Charlie");
        Map<Character, Integer> totalCharsByInitial = names2.stream()
                .collect(Collectors.groupingBy(
                        name -> name.charAt(0),
                        Collectors.summingInt(String::length)));

    }

    public static int countVowels(String str) {
        return (int) str.toLowerCase().chars().filter(c -> "aeiou".indexOf(c) >= 0).count();
    }

}
