import java.util.*;
class UnionOfTwoSortedArray {
    public static void main(String[] args) {
        int[] arr = {1,2,2,3,4,5,5};
        int[] brr = {1,2,3,4,5,6};
        List<Integer> list = new ArrayList<>();
        int i=0;
        int j=0;
        while(i<arr.length && j<brr.length) {
            if(arr[i] <= brr[j]) {
                if(list.size()!=0) {
                    int last = list.get(list.size()-1);
                    if(last!=arr[i]) {
                        list.add(arr[i]);
                    }
                }else {
                    list.add(arr[i]);
                }
                i++;
            }else {
                if(list.size()!=0) {
                    int last = list.get(list.size()-1);
                    if(last!=brr[j]) {
                        list.add(brr[j]);
                    }
                }else {
                    list.add(brr[j]);
                }
                j++;

            }
        }
        while(i<arr.length) {
            if(list.size()!=0) {
                int last = list.get(list.size()-1);
                if(last!=arr[i]) {
                    list.add(arr[i]);
                }
            }else {
                list.add(arr[i]);
            }
            i++;

        }
        while(j<brr.length) {
            if(list.size()!=0) {
                int last = list.get(list.size()-1);
                if(last!=brr[j]) {
                    list.add(brr[j]);
                }
            }else {
                list.add(brr[j]);
            }
            j++;

        }

        System.out.println(list);

    }
}