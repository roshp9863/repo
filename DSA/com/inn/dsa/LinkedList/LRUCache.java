package com.inn.dsa.LinkedList;

import java.util.HashMap;
import java.util.Map;

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
        put(newLastNode.k, newLastNode.val);
        return newLastNode.val;
        
    }
    
    public void put(int key, int value) {
        Node newLastNode = null;
        if(map.isEmpty() && map.size()<capacity) {
            handleEmpty(key, value, newLastNode);
        }
        else if(!map.containsKey(key)) {
            if(map.size()>=capacity) {
                handleEqualSize(key, value, newLastNode);
            }else {
                insertData(key, value, newLastNode);
            }
        }else {
            newLastNode = map.get(key);
            newLastNode.val = value;
            map.put(key, newLastNode);
            if(tail.equals(newLastNode)) {
                return;
            }
            if(head.equals(tail)) {
                head = newLastNode;
                tail = newLastNode; 
            }else if(head.next.equals(tail)) {
                convertHeadToTailAndTailToHead(newLastNode);
            }else {
                shiftMiddleNodeToLast(newLastNode);
            }
        }

    }

    private void shiftMiddleNodeToLast(Node newLastNode) {
        if(newLastNode.equals(head)) {
            Node newHead = newLastNode.next;
            newHead.prev = null;
            head.next = null;
            tail.next = head;
            head.prev = tail;
            tail = head;
            head = newHead;
        }else if(newLastNode.equals(tail)) {
            return;
        }else {
            Node prevNode = newLastNode.prev;
            Node nextNode = newLastNode.next;

            prevNode.next = nextNode;
            nextNode.prev = prevNode;
            tail.next = newLastNode;
            newLastNode.prev = tail;
            newLastNode.next = null;
            tail = newLastNode;
        }    
    }

    private void convertHeadToTailAndTailToHead(Node newLastNode) {
        Node nextHead = head.next;
        head.next = null;
        nextHead.prev = null;
        nextHead.next = head;
        head.prev = nextHead;
        tail = head;
        head = nextHead;
    }

    private void insertData(int key, int value, Node newLastNode) {
        newLastNode = new Node(key,value);
        map.put(key, newLastNode);
        if(head.equals(tail)) {
            head.next = newLastNode;
            newLastNode.prev = head;
            tail = newLastNode;
        }else {
            tail.next = newLastNode;
            newLastNode.prev = tail;
            newLastNode.next = null;
            tail = newLastNode;
        }
    }

    private void handleEqualSize(int key, int value, Node newLastNode) {
        if(head.equals(tail)) {
            handleSingleNode(key,value,newLastNode);
        }else {
            handleMoreThanOneNode(key, value, newLastNode);
        }
    }

    private void handleMoreThanOneNode(int key, int value, Node newLastNode) {
        newLastNode = new Node(key, value);
        Node headNext = head.next;

        if(headNext.equals(tail)) {
            tail.next = newLastNode;
            newLastNode.prev = tail;
            newLastNode.next = null;
            head.next = null;
            tail.prev = null;
            map.remove(head.k);
            tail = newLastNode;
            head = headNext;
            map.put(key, newLastNode);
        }else {
            head.next =null;
            headNext.prev = null;
            map.remove(head.k);
            map.put(key, newLastNode);
            head = headNext;
            tail.next = newLastNode;
            newLastNode.prev = tail;
            tail = newLastNode;
        }

    }

    private void handleSingleNode(int key, int value, Node newLastNode) {
        newLastNode = new Node(key, value);
        map.remove(head.k);
        head = newLastNode;
        tail = newLastNode;
        map.put(key, newLastNode);
    }

    private void handleEmpty(int key, int value, Node newLastNode) {
            newLastNode = new Node(key,value);
            map.put(key, newLastNode);
            head = newLastNode;
            tail = newLastNode;
    }

}
