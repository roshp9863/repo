import java.util.*;
class SecondLargest {
    public static void main(String[] args) {
        int[] arr = {2,1,4,5,7,6,7};
        int secondLargest = findWithBruteForce(arr);
        System.out.println(secondLargest);
        secondLargest = findWithBetterApproach(arr);
        System.out.println(secondLargest);
        secondLargest = findWithOptimal(arr);
        System.out.println(secondLargest);
    }

    
    private static int findWithOptimal(int[] arr) {
        //optimal O(n)
        int secondLargest = -1;
        int largest = arr[0];
        for(int i=0;i<arr.length;i++) {
            if(arr[i]>largest) {
                secondLargest = largest;
                largest = arr[i];
            }else if(arr[i]<largest && arr[i]<secondLargest) {
                secondLargest = arr[i];
            }
        }
        return secondLargest; 
    }


    private static int findWithBruteForce(int[] arr) {
        //brute force O(nlogn) + O(n)
        Arrays.sort(arr);
        int largest = arr[arr.length-1];
        int secondLargest = -1;
        for(int i=arr.length-2; i>=0; i--) {
            if(secondLargest<arr[i] && arr[i]!=largest) {
                secondLargest = arr[i];
            }
        }
        return secondLargest;
    }


    private static int findWithBetterApproach(int[] arr) {
        //better approach O(2n)
        int largest = arr[0];
        for(int i=1; i<arr.length; i++) {
            if(arr[i]>largest) {
                largest = arr[i];
            }
        }
        int secondLargest = -1;
        for(int i=0; i<arr.length; i++) {
            if(secondLargest<arr[i] && arr[i]!=largest) {
                secondLargest=arr[i];
            }
        }

        return secondLargest;
    }
}