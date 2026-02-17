package com.inn.dsa.kunal.LinkedListQuestions;;

public class MergeTwoSortedLL {

    Node head;
    int size;

    MergeTwoSortedLL() {
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

    public Node returnHead() {
        return head;
    }


    public void mergeTwoSortedLL(Node l1, Node l2) {
        Node dummy = new Node(-1);
        this.size=0;
        Node current = dummy;

        while(l1!=null && l2!=null) {
            if(l1.val<=l2.val) {
                current.next = l1;
                
                l1 = l1.next;
            }else {
                current.next = l2;
                l2 = l2.next;
            }
            this.size++;
            current = current.next;
        }

        if(l1!=null) {
            current.next = l1;
        }else {
            current.next = l2;
        }
        head = dummy.next;
    }

    public static void main(String[] args) {
        MergeTwoSortedLL l1 = new MergeTwoSortedLL();
        l1.insertNodeAtLast(1);
        l1.insertNodeAtLast(3);
        l1.insertNodeAtLast(4);
        l1.insertNodeAtLast(6);
        l1.print();
        Node n1 = l1.returnHead();
        MergeTwoSortedLL l2 = new MergeTwoSortedLL();
        l2.insertNodeAtLast(2);
        l2.insertNodeAtLast(5);
        l2.insertNodeAtLast(7);
        l2.print();
        Node n2 = l2.returnHead();
        l1.mergeTwoSortedLL(n1,n2);
        l1.print();

    }

}
