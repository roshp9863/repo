package com.inn.dsa.kunal.LinkedListQuestions;;

public class RemoveDuplicateFromSortedList {

    Node head;
    int size;

    RemoveDuplicateFromSortedList() {
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

    
    public void removeDuplicateFromSortedList() {
        Node temp = head;
        while(temp.next!=null) {
            if(temp.val == temp.next.val) {
                temp.next= temp.next.next;
                size--;
            }else {
                temp = temp.next;
            }

        }
    }


    public static void main(String[] args) {
        RemoveDuplicateFromSortedList l = new RemoveDuplicateFromSortedList();
        l.insertNodeAtLast(1);
        l.insertNodeAtLast(2);
        l.insertNodeAtLast(2);
        l.insertNodeAtLast(2);
        l.insertNodeAtLast(3);
        l.insertNodeAtLast(4);
        l.insertNodeAtLast(4);
        l.insertNodeAtLast(5);
        l.insertNodeAtLast(5);

        l.print();
        l.removeDuplicateFromSortedList();
        l.print();

        

    }
}
