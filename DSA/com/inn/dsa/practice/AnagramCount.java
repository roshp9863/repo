package com.inn.dsa.practice;

public class AnagramCount {

    public static int countAnagrams(String text, String word) {
      int count = 0;
      int[] charCount = new int[26]; // Assuming all lowercase characters
  
      // Build character count for the word
      for (char c : word.toCharArray()) {
        charCount[c - 'a']++;
      }
  
      int windowStart = 0;
      for (int windowEnd = 0; windowEnd < text.length(); windowEnd++) {
        // Add character to current window
        charCount[text.charAt(windowEnd) - 'a']--;
  
        // Check if all characters are present in the window
        if (isZero(charCount)) {
          count++; // Count anagram even before window reaches full word size
        }
  
        // Slide window if needed
        if (windowEnd - windowStart + 1 > word.length()) {
          charCount[text.charAt(windowStart) - 'a']++;
          windowStart++;
        }
      }
  
      return count;
    }
  
    private static boolean isZero(int[] arr) {
      for (int i : arr) {
        if (i != 0) {
          return false;
        }
      }
      return true;
    }
  
    public static void main(String[] args) {
      String text = "forxxorfxdofr";
      String word = "for";
      int anagramCount = countAnagrams(text, word);
      System.out.println("Number of anagrams of '" + word + "' in '" + text + "' : " + anagramCount);
    }
  }
  