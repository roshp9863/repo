package com.inn.dsa.LinkedList;




public class StackByLinkedList {

    StackNode head;

    private class StackNode {
        int val;
        StackNode next;

        public StackNode(int val){
            this.val = val;
        }
    }

    public void push(int val) {
        StackNode newNode = new StackNode(val);
        newNode.next = head;
        head = newNode;
    }

    public int pop(){
        int val = head.val;
        head = head.next;
        return val;
    }


    public int peek(){
        return head.val;
    }

    public static void main(String[] args) {
        
        StackByLinkedList s = new StackByLinkedList();
        s.push(1);
        s.push(2);
        s.push(3);
        s.push(4);
        s.push(5);
        int ele = s.peek();
        System.out.println(ele);
        ele = s.pop();
        System.out.println(ele);

        ele = s.peek();
        System.out.println(ele);
        ele = s.pop();
        System.out.println(ele);

    }

}
