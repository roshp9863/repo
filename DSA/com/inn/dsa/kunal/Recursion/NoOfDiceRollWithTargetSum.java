package com.inn.dsa.kunal.Recursion;

import java.util.ArrayList;
import java.util.List;

public class NoOfDiceRollWithTargetSum {

    public static void main(String[] args) {
        int target = 4;
        int[] diceNum = {1,2,3,4,5,6};
        List<String> ans = new ArrayList<>();
        diceRoll("", target, diceNum, ans);
        System.out.println(ans);
            
    }

    static void diceRoll(String processed, int target, int[] diceNum,List<String> ans) {

        if(target==0) {
            ans.add(processed);
            return;
        }

        if (target < 0) {
            return;
        }
        for (int p : diceNum) {
            diceRoll(processed + p, target - p, diceNum, ans);
        }
    
    }

}
