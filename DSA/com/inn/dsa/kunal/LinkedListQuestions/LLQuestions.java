package com.inn.dsa.kunal.LinkedListQuestions;;


public class LLQuestions {

    private Node head;

    private int size;

    LLQuestions() {
        this.size=0;
    }


    private class Node {
        int val;
        Node next;

        public Node(int val){
            this.val = val;
        }
    }

    public void insertNodeAtFirst(int val) {
        Node dummy = new Node(val);
        if(size==0) {
            head = dummy;
        }else {
            dummy.next = head;
            head = dummy;
        }
        size++;
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


    public void insertNodeAtIndex(int val, int index) {
        Node dummy = new Node(val);
        if(size==0) {
            head = dummy;
        }else {
            Node temp = head;
            while(temp!=null && index>1) {
                temp = temp.next;
                index--;
            }
            Node temp2= temp.next;
            temp.next=dummy;
            dummy.next=temp2;
        }
        size++;
    }

    public void insertNodeAtIndexFromLast(int val, int index) {
        Node dummy = new Node(val);
        index = size-index;
        if(size==0) {
            head = dummy;
        }else {
            Node temp = head;
            while(temp!=null && index>1) {
                temp = temp.next;
                index--;
            }
            Node temp2= temp.next;
            temp.next=dummy;
            dummy.next=temp2;
        }
        size++;
    }


    public void removeFirstNode() {
        if(size==0) {
            return;
        }
        if(size==1) {
            head=null;
        }
        if(size>1) {
            head = head.next;
        }
        size--;
    }

    public void removeLastNode() {
        if(size==0) {
            return;
        }
        if(size==1) {
            head=null;
        }
        Node temp = head;
        if(size>1) {
            while(temp.next.next!=null) {
                temp=temp.next;
            }
            temp.next=null;
        }
        size--;
    }

    public void removeNodeAtIndex(int index) {
        if(size==0 || index>size) {
            return;
        }
        if(size>0) {
            Node temp = head;
            while(temp.next!=null && index>1) {
                temp=temp.next;
                index--;
            }
            temp.next = temp.next.next;
        }
        size--;

    }

    public void removeNodeAtIndexFromLast(int index) {
        if(size==0 || index>size) {
            return;
        }
        index = size-index;
        if(size>0) {
            Node temp = head;
            while(temp.next!=null && index>1) {
                temp=temp.next;
                index--;
            }
            temp.next = temp.next.next;
        }
        size--;

    }

    //========================================================================

    public void insertAtLastUsingRecur(int val) {
       
        head =  insertAtLastUsingRecur(val, head);
        
    }

    private Node insertAtLastUsingRecur(int val, Node temp) {
        if(temp==null) {
            size++;
            return new Node(val);
        }
        temp.next = insertAtLastUsingRecur(val, temp.next);
        return temp;
    }



    public void insertAtIndexUsingRecur(int val, int index) {
       
        head =  insertAtIndexUsingRecur(val,index,  head);
        
    }

    private Node insertAtIndexUsingRecur(int val, int index, Node temp) {
        if(index==0) {
            size++;
            Node n = new Node(val);
            n.next = temp;
            return n;
        }
        temp.next = insertAtIndexUsingRecur(val,index-1, temp.next);
        return temp;
    }


    public void print() {
        Node dummy = head;
        while(dummy!=null) {
            System.out.print(dummy.val + " -> ");
            dummy = dummy.next;
        }
        System.out.print(" null ");
        System.out.println();
    }
    

    public static void main(String[] args) {
        LLQuestions l = new LLQuestions();
        l.insertNodeAtFirst(3);
        l.insertNodeAtFirst(8);
        l.insertNodeAtFirst(5);
        l.insertNodeAtFirst(2);
        l.insertNodeAtLast(14);
        l.insertNodeAtIndex(6, 3);
        l.insertNodeAtIndexFromLast(19, 4);
        l.removeFirstNode();
        l.removeLastNode();
        l.removeNodeAtIndex(2);
        l.insertNodeAtFirst(8);
        l.insertNodeAtFirst(5);
        l.insertNodeAtFirst(2);
        l.removeNodeAtIndexFromLast(3);

        l.print();

        //==================================================

        l.insertAtLastUsingRecur(212);
        l.insertAtIndexUsingRecur(33, 3);
        l.print();

        //==================================================

    }

}
