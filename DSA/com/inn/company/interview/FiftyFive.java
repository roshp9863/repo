package com.inn.company.interview;

import java.util.HashSet;
import java.util.Set;

public class FiftyFive {

    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("mad");
        set.add("bad");
        set.add("fool");
        set.add("stupid");

        String  str = "she is mad and stupid girl";
        StringBuilder st = new StringBuilder();
        String arr[] = str.split(" ");
        for(int i=0; i<arr.length; i++){
            if(set.contains(arr[i])) {
                int len = arr[i].length();
                for(int j = 0; j < len; j++) {
                    st.append("*");
                }
                st.append(" ");

            }else {           
                st.append(arr[i]);
                st.append(" ");
            }
        }
        
        System.out.println(st.toString().trim());

        //convert the above code into a single line
        //write even odd logic


    }


}
