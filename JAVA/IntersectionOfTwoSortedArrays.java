import java.util.*;

class IntersectionOfTwoSortedArrays {
    public static void main(String[] args) {
        int[] arr = {1,2,2,3,3,4,5,6};
        int[] brr = {2,3,3,5,6,6,7};
        int i=0;
        int j=0;
        List<Integer> list = new ArrayList<>();    
        while(i<arr.length && j<brr.length) {
            if(arr[i]==brr[j]) {
                list.add(arr[i]);
                i++;
                j++;
            }else{
                i++;
            } 
        }
        System.out.println(list);
    }
}