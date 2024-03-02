import java.util.*;

class Solution {
    public static String longestCommonPrefix() {
        String[] strs = new String[] { "flower", "flow", "flight" }; // Correcting the array initialization
        Arrays.sort(strs); 
        int k = 0;
        String str = "";
        int i = 0;
        String s = strs[strs.length - 1];
        int j = strs.length - 1;
        while (i < s.length()) {
            if (strs[k].charAt(i) == strs[j].charAt(i)) {
                str =  str + strs[k].charAt(i);
                i++;
                continue;
            } else {
                break;
            }
        }
        return str;
    }

    public static void main(String[] args) {
        System.out.println(longestCommonPrefix());
    }

}
