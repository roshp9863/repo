package com.inn.dsa.LinkedList;

import java.util.HashMap;
import java.util.Map;

public class LRU {
    class Node {
        Node next;
        Node prev;
        int val; 
        int k;
        Node(int key, int val) {
            this.k = key;
            this.val = val;
        }
    }

    class LRUCache {

        Node head, tail;
        Map<Integer, Node> map = new HashMap<>();
        int capacity;
        public LRUCache(int capacity) {
            this.capacity = capacity;
        }
        
        public int get(int key) {
            if(!map.containsKey(key)) {
                return -1;
            }
            Node newLastNode = map.get(key);
            System.out.println("GET NEWLASTNODE  "+ newLastNode.k);
            put(newLastNode.k, newLastNode.val);
            return newLastNode.val;
            
        }
        
        public void put(int key, int value) {
            Node newLastNode;
            if(map.isEmpty() && map.size()<capacity) {
                newLastNode = new Node(key,value);
                map.put(key, newLastNode);
                head = newLastNode;
                tail = newLastNode;
                System.out.println("head "+ head.k);
            
                return;
            }
            else if(!map.containsKey(key)) {
                System.out.println("MAP Size : "+ map.size() + " CAPACITY : "+ capacity);
                System.out.println("HEAD : " + head);

                if(map.size()>=capacity && head != null) {
                    System.out.println("HEAD KEY : "+ head.k);
                    map.remove(head.k);
                    Node newHead = head.next;
                    if(newHead != null) {
                        head.next = null;
                        newHead.prev = null;
                        head = newHead;
                    }else {
                        System.out.println("WE ARE IN ELSE");
                        head = null;
                        tail = null;
                    }
                }
                System.out.println("HEAD is NUll KEy : "+ key);

                newLastNode = new Node(key,value);
                map.put(key, newLastNode);
            }else {
                System.out.println("I AM IN ELSE");
                System.out.println("HEAD : " + head.k);

                newLastNode = map.get(key);
                newLastNode.val = value;
                newLastNode.k = key;
                System.out.println("NewlastNode "+ newLastNode.k);

                if(newLastNode.equals(head)) {
                    System.out.println("HEAD  and new Last node : " + head.k);

                    Node nextNode = newLastNode.next;
                    if(nextNode !=null ) 
                        nextNode.prev = null;
                    newLastNode.next=null;
                    if(nextNode!=null) head = nextNode;
                    System.out.println("head "+ head.k);

                }else if(!newLastNode.equals(tail) && !newLastNode.equals(head)) {

                    Node prevNode = newLastNode.prev;
                    Node nextNode = newLastNode.next;
                    if(prevNode !=null ) prevNode.next = null;
                    if(nextNode !=null ) nextNode.prev = null;
                    newLastNode.next=null;
                    newLastNode.prev=null;

                }
                System.out.println("I AM IN last ELSE "+ head);
                

            }
            if(head==null && tail==null) {
                System.out.println("I AM COMING HERE");
                head = newLastNode;
                tail = newLastNode;
            }     

            if(tail!=null) {
                if(!tail.equals(newLastNode)) {
                    tail.next = newLastNode;
                    newLastNode.prev = tail;
                    tail = newLastNode;
                    tail.next=null;
                }else {
                    tail = newLastNode;
                }
            }
            System.out.println("I AM IN last of method");


        }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
}
