package com.inn.dsa.adityaverma.recursion.RecursionProblems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortAnArray {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(1);
        list.add(5);
        list.add(2);
        list.add(4);

        sort(list);
        System.out.println(list);
    }

    static void sort(List<Integer> list) {
        if (list.size() <= 1) return;

        int last = list.remove(list.size() - 1);
        sort(list);
        insert(list, last);
    }

    static void insert(List<Integer> list, int value) {
        if (list.size() == 0 || list.get(list.size() - 1) <= value) {
            list.add(value);
            return;
        }

        int last = list.remove(list.size() - 1);
        insert(list, value);
        list.add(last);
    }
}
