class BST {
    public class Node {
        private int value;
        private Node left;
        private Node right;
        private int height;

        public Node(int value) {
            this.value=value;
        }
        public int getValue() {
            return this.value;
        }
    }
    private Node root;
    public BST() {

    }
    public int height(Node node) {
        if(node==null) {
            return -1;
        }
        return node.height;
    }

    public boolean is_Empty() {
        return root == null; 
    }

    public void display() {
        display(root, "ROOT NODE: ");
    }

    private void display(BST.Node node, String details) {
        if(node==null) {
            return;
        }
        System.out.println(details + node.getValue());
        display(node.left, "Left child of " + node.getValue() + " : ");
        display(node.right, "Right child of " + node.getValue() + " : ");
    }

    public void insert(int value) {
        root = insert(value, root);
    }
    private Node insert(int value, Node node) {
        if(node==null) {
            node = new Node(value);
            return node;
        }

        if(value <= node.value) {
            node.left = insert(value, node.left);
        }

        if(value > node.value) {
            node.right=insert(value, node.right);
        }
        node.height = Math.max(height(node.left), height(node.right)) +1;
        return node;
    }
}