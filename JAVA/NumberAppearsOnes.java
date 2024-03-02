class NumberAppearsOnes {
    public static void main(String[] args) {
        int[] arr = {1,1,2,3,3,4,5,5,4};
        int num =0;
        for(int i =0; i<arr.length; i++) {
            num = num^arr[i];
        }
        System.out.println(num);
    }
}
