package com.inn.dsa.kunal.LinkedListQuestions;;

public class MergeSortLL {

    private static class Node {
        int val;
        Node next;
        Node(int val) { this.val = val; }
    }

    private Node head;
    private int size;

    public MergeSortLL() {
        this.size = 0;
        this.head = null;
    }

    public void insertNodeAtLast(int val) {
        Node newNode = new Node(val);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
        size++;
    }

    public void print() {
        print(head);
    }

    public void print(Node node) {
        Node tmp = node;
        while (tmp != null) {
            System.out.print(tmp.val + " -> ");
            tmp = tmp.next;
        }
        System.out.println("null (size=" + sizeForNode(node) + ")");
    }

    private int sizeForNode(Node node) {
        int count = 0;
        Node tmp = node;
        while (tmp != null) {
            count++;
            tmp = tmp.next;
        }
        return count;
    }

    public Node returnHead() {
        return head;
    }

    /**
     * Perform merge sort on the linked list starting at 'node'.
     */
    public static Node mergeSort(Node node) {
        if (node == null || node.next == null) {
            return node;
        }
        // Split list into two halves
        Node mid = middleNode(node);
        Node rightHead = mid.next;
        mid.next = null;

        Node leftSorted = mergeSort(node);
        Node rightSorted = mergeSort(rightHead);
        return mergeTwoSortedLL(leftSorted, rightSorted);
    }

    /**
     * Merge two sorted linked lists and return head of merged list.
     */
    public static Node mergeTwoSortedLL(Node l1, Node l2) {
        Node dummy = new Node(-1);
        Node current = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                current.next = l1;
                l1 = l1.next;
            } else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }
        if (l1 != null) {
            current.next = l1;
        } else {
            current.next = l2;
        }
        return dummy.next;
    }

    /**
     * Finds the middle node of the list (Tortoise-Hare).
     * For even length, returns the first of the two middles.
     */
    private static Node middleNode(Node head) {
        Node slow = head;
        Node fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    public static void main(String[] args) {
        MergeSortLL list = new MergeSortLL();
        list.insertNodeAtLast(4);
        list.insertNodeAtLast(1);
        list.insertNodeAtLast(6);
        list.insertNodeAtLast(16);
        list.insertNodeAtLast(21);
        list.insertNodeAtLast(13);

        System.out.print("Original list: ");
        list.print();

        Node sortedHead = mergeSort(list.returnHead());
        System.out.print("Sorted list:   ");
        // Since size may differ after split, print directly traversing
        MergeSortLL tempList = new MergeSortLL();
        tempList.head = sortedHead;
        tempList.print(sortedHead);
    }
}
