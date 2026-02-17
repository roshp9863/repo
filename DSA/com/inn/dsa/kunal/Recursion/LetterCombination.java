package com.inn.dsa.kunal.Recursion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LetterCombination {

    public static void main(String[] args) {
        List<String> rs = letterCombinations("2323");
        System.out.println(rs);
    }

    public static List<String> letterCombinations(String digits) {
        List<String> ans = new ArrayList<>();
        if(digits.length()==0) {
            return ans;
        }
        Map<Character,String> map = new HashMap<>();
        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");
        combination("", digits, map, ans);
        return ans;
        
    }

    public static void combination(String processed, String unprocessed, Map<Character,String> map, List<String> list) {
        if(unprocessed.isEmpty()) {
            list.add(processed);
            return;
        }

        char digit = unprocessed.charAt(0);
        String letters = map.get(digit);

        for(char letter : letters.toCharArray()) {
            combination(processed + letter, unprocessed.substring(1), map, list);
        }

    }

}
