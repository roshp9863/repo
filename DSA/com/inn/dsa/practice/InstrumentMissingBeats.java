package com.inn.dsa.practice;

import java.util.*;

public class InstrumentMissingBeats {
    public List<Integer> minBeats(int[] nums, int target) {
        Arrays.sort(nums);
        long miss = 1;
        int i = 0, n = nums.length;
        List<Integer> added = new ArrayList<>();
        
        while (miss <= target) {
            if (i < n && nums[i] <= miss) {
                miss += nums[i];
                i++;
            } else {
                added.add((int)miss);
                miss += miss;
            }
        }
        
        return added;
    }

    public static void main(String[] args) {
        InstrumentMissingBeats s = new InstrumentMissingBeats();
        int[] beats = {1,1,1};
        System.out.println(s.minBeats(beats, 20)); // [4, 8, 16]
    }
}