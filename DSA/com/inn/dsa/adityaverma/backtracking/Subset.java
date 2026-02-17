package com.inn.dsa.adityaverma.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Subset {
    public static void main(String[] args) {
        List<Integer> ip = Arrays.asList(2,10,-2,4,-5);
        List<List<Integer>> rs = new ArrayList<>();
        subset(0, new ArrayList<Integer>(), ip, rs);
        int[] rs1 = new int[1];
        subsetSum(0,0, ip, 6, rs1); //gpt way
        int[] result = new int[1];
        subsetSumMyWay(0, new ArrayList<Integer>(), ip, 6, result);
        System.out.println(rs);
    }

    private static void subset(int start, ArrayList<Integer> current,  List<Integer> ip, List<List<Integer>> rs) {
        rs.add(new ArrayList<>(current));
        current.stream().collect(Collectors.summarizingInt(null));
        for(int i=start; i<ip.size(); i++) {
            current.add(ip.get(i));
            subset(i+1, current, ip, rs);
            current.remove(current.size()-1);

        }
    }

    private static void subsetSum(int index, int currentSum,  List<Integer> ip, int target, int[] rs) {
        if(target==currentSum) {
            rs[0] =1;
            return;
        }
        if (rs[0] == 1 || index == ip.size()) {
            return;
        }
        subsetSum(index + 1, currentSum + ip.get(index), ip, target, rs);

        subsetSum(index + 1, currentSum, ip, target, rs);

    }

    private static void subsetSumMyWay(int start, ArrayList<Integer> current,  List<Integer> ip, int target, int[] result) {
        int sum = current.stream().mapToInt(Integer::intValue).sum();
        if(target==sum) {
            result[0] =1;
            return;
        }
        for(int i=start; i<ip.size(); i++) {
            current.add(ip.get(i));
            subsetSumMyWay(i+1, current, ip, target,result);
            current.remove(current.size()-1);
        }
    }
}
