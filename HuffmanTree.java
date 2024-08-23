import java.util.Optional;

public class HuffmanTree {
    /**
     * Public Attributes:
     * ===========
     * symbol: symbol stored in this Huffman tree node, if any
     * number: the number of this Huffman tree node
     * left: the left subtree of this Huffman tree node
     * right: the right subtree of this Huffman tree node
     */
    public Integer symbol;
    public Integer number;
    public HuffmanTree left;
    public HuffmanTree right;

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

    // Return a string representation of this HuffmanTree
    @Override
    public String toString() {
        return "HuffmanTree(" + symbol + ", " + left + ", " + right + ")";
    }

    // Check if this HuffmanTree is a leaf
    public boolean isLeaf() {
        return left == null && right == null;
    }

    // Return the number of nodes required to represent this Huffman tree
    // Precondition: This Huffman tree is already numbered.
    public byte[] numNodesToBytes() {
        return new byte[]{(byte) (number + 1)};
    }



    // Test Space (REMOVE IN FINAL VERSION)
    public static void main(String[] args) {
        HuffmanTree a = new HuffmanTree(4);
        HuffmanTree b = new HuffmanTree(4);

        System.out.println(a.equals(b)); // Should print: true

        b = new HuffmanTree(5);
        System.out.println(a.equals(b)); // Should print: false

        System.out.println(a.isLeaf()); // Should print: true
        System.out.println(a.toString()); // Should print: HuffmanTree(4, null, null)

        HuffmanTree c = new HuffmanTree(6, a, b);
        System.out.println(c.toString());// Should print: HuffmanTree(6, HuffmanTree(4, null, null), HuffmanTree(5, null, null))
    }
}
