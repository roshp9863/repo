import java.util.Arrays;

class ZerosToTheEnd {
    public static void main(String[] args) {
        int[] arr = {1,2,1,3,0,8,7,0,0,9,5};
        int i = 0;
        int j =-1;
        while(i<arr.length) {
            if(arr[i]!=0) {
                i++;
                continue;
            }else {
                j=i;
                break;
            } 
        }
        i=i+1;
        while(i<arr.length) {
            if(arr[i]==0) {
                i++;
            }else {
                int temp = arr[j];
                arr[j] = arr[i];
                arr[i] = temp;
                j++;  
            }
        }
        System.out.println(Arrays.toString(arr));
    }
}