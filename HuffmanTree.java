import java.util.Optional;

public class HuffmanTree {
    /**
     * Public Attributes:
     * ===========
     * symbol: symbol located in this Huffman tree node, if any
     * number: the number of this Huffman tree node
     * left: left subtree of this Huffman tree
     * right: right subtree of this Huffman tree
     */
    private Integer symbol;
    private Integer number;
    private HuffmanTree left;
    private HuffmanTree right;

    // Constructor
    public HuffmanTree(Integer symbol, HuffmanTree left, HuffmanTree right) {
        this.symbol = symbol;
        this.left = left;
        this.right = right;
        this.number = null;
    }

    // Constructor for leaf
    public HuffmanTree(Integer symbol) {
        this(symbol, null, null); // Call the main constructor with null for left and right
    }

    // Check if this HuffmanTree is equivalent to another HuffmanTree
    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        HuffmanTree otherTree = (HuffmanTree) otherObj;
        return symbol.equals(otherTree.symbol) 
                && ((left == null && otherTree.left == null) || (left != null && left.equals(otherTree.left))) 
                && ((right == null && otherTree.right == null) || (right != null && right.equals(otherTree.right)));

    }

    // HuffmanTree nodes are never considered less than other nodes
    public boolean lessThan(HuffmanTree other) {
        return false; // arbitrarily say that one node is never less than another
    }

    // Return constructor-style string representation of this HuffmanTree
    @Override
    public String toString() {
        return "HuffmanTree(" + symbol + ", " + left + ", " + right + ")";
    }

    // Check if this HuffmanTree is a leaf
    public boolean isLeaf() {
        return left == null && right == null;
    }

    // Return the number of nodes required to represent this Huffman tree
    // Precondition: this Huffman tree is already numbered.
    public byte[] numNodesToBytes() {
        return new byte[]{(byte) (number + 1)};
    }



    // Test Space
    public static void main(String[] args) {
        // Example usage of the HuffmanTree class

        // Create a leaf node with symbol 4
        HuffmanTree a = new HuffmanTree(4);

        // Create another leaf node with symbol 4 and compare with a
        HuffmanTree b = new HuffmanTree(6);
        System.out.println(a.equals(b)); // Should print: true
    }
}
