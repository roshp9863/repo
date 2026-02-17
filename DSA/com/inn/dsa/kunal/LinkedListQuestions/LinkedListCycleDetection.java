package com.inn.dsa.kunal.LinkedListQuestions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LinkedListCycleDetection {

    Node head;
    int size;

    LinkedListCycleDetection() {
        this.size=0;
    }

    private class Node {
        int val;
        Node next;

        public Node(int val){
            this.val = val;
        }
    }

    public void insertNodeAtLast(int val) {
        Node dummy = new Node(val);
        if(size==0) {
            head = dummy;
        }else {
            Node temp = head;
            while(temp.next!=null) {
                temp = temp.next;
            }

            temp.next = dummy;
        }
        size++;
    }

    public void print() {
        Node dummy = head;
        while(dummy!=null) {
            System.out.print(dummy.val + " -> ");
            dummy = dummy.next;
        }
        System.out.print(" null ");
        System.out.println("SIZE : "+ size);
    }

    public void print(Node n1) {
        Node dummy = n1;
        while(dummy!=null) {
            System.out.print(dummy.val + " -> ");
            dummy = dummy.next;
        }
        System.out.print(" null ");
        System.out.println("SIZE : "+ size);
    }


    public Node returnHead() {
        return head;
    }

    private static Node createACycleAtIndex(int i, Node n) {

        Node temp = n;
        while (i>0) {
            temp = temp.next;
            i--;
        }
        Node connect = temp;
        while(temp.next!=null) {
            temp = temp.next;
        }
        temp.next = connect; 
        return n;   

    }

    private static boolean detectACyle(Node n1) {
        Node f1 = n1;
        Node s2 = n1;

        while(f1.next!=null && s2.next.next !=null) {
            f1 = f1.next;
            s2 = s2.next.next;
            if(f1==s2) {
                return true;
            }
        }
        return false;    
    }

    private static int detectLengthOfACyle(Node n1) {
        Node f1 = n1;
        Node s2 = n1;
        int len =0;
        while(f1.next!=null && s2.next.next !=null) {
            f1 = f1.next;
            s2 = s2.next.next;
            if(f1==s2) {
                len++;
                f1 = f1.next;
                while(f1!=s2) {
                    f1=f1.next;
                    len++;
                }
                return len;
            }
        }
        return len;
    }

    private static void cycleStartNodeScaler(Node n1) {
        Node f1 = n1;
        Node s2 = n1;
        Node y = n1;
        while(f1.next!=null && s2.next.next !=null) {
            f1 = f1.next;
            s2 = s2.next.next;
            if(f1==s2) {
                while(s2!=y) {
                    s2= s2.next;
                    y=y.next;
                }
                System.out.println("CYCLE START NODE Scaler : "+ y.val);

                return;

            }
        }
    }

    private static void cycleStartNode(int len, Node n1) {
        Node s = n1;
        while(len>0) {
            s=s.next;
            len--;
        }
        Node f = n1;
        while(s!=f) {
            s=s.next;
            f=f.next;
        }

        System.out.println("CYCLE START NODE : "+ s.val);

    }

    private static boolean isHappy(int i) {
        Set<Integer> set = new HashSet<>();
        set.add(i);
        return isHappy(i, set);
    }


    private static boolean isHappy(int num, Set<Integer> set) {
        int sum=0;
        if(num==1) {
            return true;
        }
        while(num>0) {
            int remain = num%10;
            sum = sum + (remain * remain);
            num = num/10;
        }
        if(set.contains(sum)) {
            return false;
        }
        set.add(sum);
        return isHappy(sum, set);
    }

    private static boolean isHappyCycle(int n) {
        int slow = n;
        int fast = n;

        slow = square(slow);
        fast = square(square(fast));
        while(slow!=fast) {
            slow = square(slow);
            fast = square(square(fast));
        }
        if(slow == 1){
            return true;
        }
        return false;
    }


    private static int square(int num) {
        int sum =0;
        while(num>0) {
            int remain = num%10;
            sum = sum + (remain * remain);
            num = num/10;
        }
        return sum;

    }

    private static Node middleNode(Node n2) {

        Node fast = n2;
        Node slow = n2;

        while(fast!=null && fast.next!=null) {
            slow = slow.next;
            fast = fast.next.next;
            String.join("/");
        }
        return slow;
    }


    public static void main(String[] args) {
        LinkedListCycleDetection l1 = new LinkedListCycleDetection();
        l1.insertNodeAtLast(1);
        l1.insertNodeAtLast(3);
        l1.insertNodeAtLast(4);
        l1.insertNodeAtLast(6);
        l1.insertNodeAtLast(8);
        l1.insertNodeAtLast(7);
        l1.print();
        Node n = l1.returnHead();

        Node n1 = createACycleAtIndex(2, n);    
        // l1.print();
        // l1.print(n1);

        boolean flag = detectACyle(n1);
        System.out.println("N1 IS CYCLE DETECTED : "+ flag);


        LinkedListCycleDetection l2 = new LinkedListCycleDetection();
        l2.insertNodeAtLast(1);
        l2.insertNodeAtLast(3);
        l2.insertNodeAtLast(4);
        l2.insertNodeAtLast(6);
        l2.insertNodeAtLast(8);
        l2.insertNodeAtLast(7);
        Node n2 = l2.returnHead();

        boolean flag2 = detectACyle(n2);
        System.out.println("N2 IS CYCLE DETECTED : "+ flag2);

        int length = detectLengthOfACyle(n1);
        System.out.println("N1 CYCLE Length : "+ length);

        cycleStartNode(length, n1);
        cycleStartNodeScaler(n1);  

        boolean isNumHappy = isHappy(19);
        System.out.println("Is Num Happy : "+ isNumHappy);
        isNumHappy = isHappy(2);
        System.out.println("Is Num Happy : "+ isNumHappy);

        isNumHappy = isHappyCycle(19);
        System.out.println("Is Num Happy With Cycle : "+ isNumHappy);
        isNumHappy = isHappyCycle(2);
        System.out.println("Is Num Happy With Cycle : "+ isNumHappy);

        Node middle = middleNode(n2);
        System.out.println("MIDDLE NODE VAL : "+ middle.val);




    }






}
