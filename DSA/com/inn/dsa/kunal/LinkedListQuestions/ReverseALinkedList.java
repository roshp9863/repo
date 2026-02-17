package com.inn.dsa.kunal.LinkedListQuestions;;

public class ReverseALinkedList {

    Node head;
    int size;

    ReverseALinkedList() {
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

    public Node reverseALinkedList(Node head) {
        if(head == null || head.next == null) {
            return head;
        }
        Node newHead = reverseALinkedList(head.next);
        head.next.next = head;
        head.next=null;
        return newHead;

    }

    public Node reverseALinkedListWithoutRecursion(Node head) {

        Node previous = null;
        Node current = head;
        Node next = current.next;
        while(current !=null) {

            current.next = previous;
            previous = current;
            current = next;

            if(next!=null) {
                next = current.next;
            }
        }

        return previous;

    }

    private Node reverseBetween(int left, int right) {
        if(left == right) {
            return head;
        }
        Node current = head;
        Node previous = null;

        for(int i=0; current!=null && i<left-1; i++) {
            previous = current;
            current = current.next;
        }

        Node lastNode = previous;
        Node newNode = current;
        Node next = null;
        for(int i=0; current!=null && i < right-left+1; i++) {
            next = current.next;
            current.next = previous;
            previous = current;
            current = next;

        }

        if(lastNode!=null) {
            lastNode.next = previous;
        }else {
            head = previous;
        }

        newNode.next = current;
        return head;
    }




    private boolean isPalindrome(Node palin) {
        System.out.println("I AM IN PALINDROME METHOD");
        print(palin);

        Node slow = palin; 
        Node fast = palin;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        Node second = reverseALinkedList(slow);
        Node first  = palin;
        while (second != null) {
            if (first.val != second.val) return false;
            first  = first.next;
            second = second.next;
        }
        return true;

    }

    private Node reorderTheList() {
        print();
        Node slow = head; 
        Node fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        Node second = reverseALinkedList(slow);
        Node first  = head;
        Node temp = new Node(-1);
        Node dummy = temp;
        boolean takeFirst = true;
        while(second!=null && first!=null) {
            if(takeFirst) {
                dummy.next = first;
                first = first.next;
            }else {
                dummy.next = second;
                second = second.next;
            }
            dummy = dummy.next;

            takeFirst = !takeFirst;

        }
        return temp.next;

    }




    public static void main(String[] args) {
        ReverseALinkedList r = new ReverseALinkedList();
        r.insertNodeAtLast(5);
        r.insertNodeAtLast(4);
        r.insertNodeAtLast(3);
        r.insertNodeAtLast(2);
        r.insertNodeAtLast(1);
        
        Node n = r.returnHead();
        r.print(n);
        Node n1 = r.reverseALinkedList(n);
        r.print(n1);

        Node n2 = r.reverseALinkedListWithoutRecursion(n1);
        r.print(n2);

        Node n3 = r.reverseBetween(2, 4);
        r.print(n3);


        ReverseALinkedList r1 = new ReverseALinkedList();
        r1.insertNodeAtLast(1);
        r1.insertNodeAtLast(2);
        r1.insertNodeAtLast(3);
        r1.insertNodeAtLast(2);
        r1.insertNodeAtLast(1);
        Node palin = r1.returnHead();

        boolean isPalin = r1.isPalindrome(palin);
        System.out.println("ISPALIN : "+ isPalin);



        Node reorder = r.reorderTheList();
        r.print(reorder);


    }




}
