package com.inn.dsa.practice;
import java.util.*;
class BinaryTree {

    public BinaryTree() {

    }

    private static class Node {
        int value;
        Node right;
        Node left;
        public Node(int value) {
            this.value = value;
        }
    }
    private Node root;

    //insert Elements 
    public void populate(Scanner sc) {
        System.out.println("ENTER THE ROOT VALUE : ");
        int value = sc.nextInt();
        root = new Node(value);
        populate(sc, root);
    }

    private void display() {
        display(root, "");
    }

    private void display(BinaryTree.Node node, String indent) {
        if(node == null) {
            return;
        }
        System.out.println(indent + node.value);
        display(node.left, indent + "\t");
        display(node.right, indent + "\t");
    }

    private void populate(Scanner sc, BinaryTree.Node node) {
        System.out.println("Do you want to enter left of " + node.value);
        boolean left = sc.nextBoolean();
        if(left) {
            System.out.println("Enter the value of the left of "+ node.value);
            int value = sc.nextInt();
            node.left = new Node(value);
            populate(sc, node.left);

        }
        System.out.println("Do you want to enter right of " + node.value);
        boolean right = sc.nextBoolean();
        if(right) {
            System.out.println("Enter the value of the right of "+ node.value);
            int value = sc.nextInt();
            node.right = new Node(value);
            populate(sc, node.right);

        }
    }

    public void preetyDisplay() {
        preetyDisplay(root,0);
    }

    private void preetyDisplay(BinaryTree.Node node, int level) {

        if(node==null) {
            return;
        }
        preetyDisplay(node.right, level+1);

        if(level!=0) {
            for(int i=0; i<level-1; i++) {
                System.out.print("|\t");    
            }
            System.out.println("|------>" + node.value);
        }else{
            System.out.println(node.value);
        }
        preetyDisplay(node.left, level+1);

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BinaryTree tree = new BinaryTree();
        tree.populate(sc);
        System.out.println();
        System.out.println();
        tree.preetyDisplay();
        System.out.println();
        System.out.println();
        // tree.findData(40, root);

    }

    // private boolean findData(int find, Node root) {
    //     while(root!=null) {
    //         if(root.data==find) {
    //             return true;
    //         }else if(find<=root.data) {
    //             findData(find, root.left);
    //         }else {
    //             findData(find, root.right);
    //         }
    //     }

    //     return false;
    // }
}


