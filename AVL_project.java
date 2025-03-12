
public class AVL_project {
    public static void main(String[] args) {
        AVLTree fieldTree = new AVLTree();

        // Inserting Fields
        fieldTree.insert(new Field(10, 40.5, 20.3, 30.1, 50.8, 6.5));
        fieldTree.insert(new Field(20, 35.0, 15.0, 25.5, 45.2, 7.0));
        fieldTree.insert(new Field(5, 25.3, 10.2, 20.7, 35.4, 5.8));

        // Displaying Fields
        System.out.println("\n=== All Fields ===");
        fieldTree.printAllFields();

        // Searching for a Field
        System.out.println("\n=== Search Zone ID 10 ===");
        Field field = fieldTree.search(10);
        System.out.println(field != null ? field : "Field not found!");

        // Updating a Field
        System.out.println("\n=== Updating Zone ID 10 ===");
        fieldTree.search(10).waterLevel = 50.0; // Updating water level
        System.out.println(fieldTree.search(10));

        // Deleting a Field
        System.out.println("\n=== Deleting Zone ID 5 ===");
        fieldTree.delete(5);

        // Displaying Fields After Deletion
        System.out.println("\n=== All Fields After Deletion ===");
        fieldTree.printAllFields();
    }
}


class Field {
    int zoneId;
    double waterLevel;
    double nitrogenLevel;
    double phosphorusLevel;
    double potassiumLevel;
    double phLevel;

    //constructor class
    public Field(int zoneId, double waterLevel, double nitrogenLevel, 
                double phosphorusLevel, double potassiumLevel, double phLevel) {
        this.zoneId = zoneId;
        this.waterLevel = waterLevel;
        this.nitrogenLevel = nitrogenLevel;
        this.phosphorusLevel = phosphorusLevel;
        this.potassiumLevel = potassiumLevel;
        this.phLevel = phLevel;
    }
    
    @Override
    public String toString() {
        return "Field Zone ID: " + zoneId +
               "\nWater Level: " + waterLevel +
               "\nNitrogen Level: " + nitrogenLevel +
               "\nPhosphorus Level: " + phosphorusLevel +
               "\nPotassium Level: " + potassiumLevel +
               "\npH Level: " + phLevel;
    }
}

class Node {
    Field data;
    Node left;
    Node right;
    int height;
    
    public Node(Field data) {
        this.data = data;
        this.height = 1;
        this.left = null;
        this.right = null;
    }
}

class LinkedList {
    Node root;
    
    public LinkedList() {
        this.root = null;
    }
    
    public int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }
    
    public int getBalanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }
    
    public Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        
        x.right = y;
        y.left = T2;
        
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        
        return x;
    }
    
    public Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        
        y.left = x;
        x.right = T2;
        
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        
        return y;
    }
    
    public Node insert(Node node, Field field) {
        if (node == null) {
            return new Node(field);
        }
        
        if (field.zoneId < node.data.zoneId) {
            node.left = insert(node.left, field);
        } else if (field.zoneId > node.data.zoneId) {
            node.right = insert(node.right, field);
        } else {
            return node;
        }
        
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        
        int balance = getBalanceFactor(node);
        
        // Left Left Case
        if (balance > 1 && field.zoneId < node.left.data.zoneId) {
            return rightRotate(node);
        }
        
        // Right Right Case
        if (balance < -1 && field.zoneId > node.right.data.zoneId) {
            return leftRotate(node);
        }
        
        // Left Right Case
        if (balance > 1 && field.zoneId > node.left.data.zoneId) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        
        // Right Left Case
        if (balance < -1 && field.zoneId < node.right.data.zoneId) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        
        return node;
    }
    
    public Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }
    
    public Node delete(Node root, int zoneId) {
        if (root == null) {
            return null;
        }
        
        if (zoneId < root.data.zoneId) {
            root.left = delete(root.left, zoneId);
        } else if (zoneId > root.data.zoneId) {
            root.right = delete(root.right, zoneId);
        } else {
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;
            
            Node temp = minValueNode(root.right);
            root.data = temp.data;
            root.right = delete(root.right, temp.data.zoneId);
        }
        
        if (root == null) {
            return null;
        }
        
        root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;
        
        int balance = getBalanceFactor(root);
        
        // Left Left Case
        if (balance > 1 && getBalanceFactor(root.left) >= 0) {
            return rightRotate(root);
        }
        
        // Left Right Case
        if (balance > 1 && getBalanceFactor(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        
        // Right Right Case
        if (balance < -1 && getBalanceFactor(root.right) <= 0) {
            return leftRotate(root);
        }
        
        // Right Left Case
        if (balance < -1 && getBalanceFactor(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        
        return root;
    }
    
    public Node search(Node root, int zoneId) {
        if (root == null || root.data.zoneId == zoneId) {
            return root;
        }
        
        if (zoneId < root.data.zoneId) {
            return search(root.left, zoneId);
        }
        
        return search(root.right, zoneId);
    }
    
    public void inOrderTraversal(Node node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.println(node.data);
            System.out.println("----------------------");
            inOrderTraversal(node.right);
        }
    }
}

class AVLTree {
    private LinkedList list;
    
    public AVLTree() {
        list = new LinkedList();
    }
    
    public void insert(Field field) {
        list.root = list.insert(list.root, field);
    }
    
    public void delete(int zoneId) {
        Field field = search(zoneId);
        if (field == null) {
            System.out.println("Field not found!");
            return;
        }
        list.root = list.delete(list.root, zoneId);
        System.out.println("Field deleted successfully!");
    }
    
    public Field search(int zoneId) {
        Node result = list.search(list.root, zoneId);
        return result != null ? result.data : null;
    }
    
    public void printAllFields() {
        if (list.root == null) {
            System.out.println("No fields available.");
            return;
        }
        list.inOrderTraversal(list.root);
    }
}