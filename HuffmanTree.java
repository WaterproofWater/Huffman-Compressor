
public class HuffmanTree {
    /**
     * Public Attributes:
     * ===========
     * symbol: symbol stored in this Huffman tree node, if any
     * size: the size of this Huffman tree node
     * left: the left subtree of this Huffman tree node
     * right: the right subtree of this Huffman tree node
     */
    public Integer symbol;
    public Integer size;
    public HuffmanTree left;
    public HuffmanTree right;

    // Constructor
    public HuffmanTree(Integer symbol, HuffmanTree left, HuffmanTree right, Integer size) {
        this.symbol = symbol;
        this.left = left;
        this.right = right;
        this.size = size;
    }

    // Constructor for leaf node
    public HuffmanTree(Integer symbol, Integer size) {
        this(symbol, null, null, size); // Call the main constructor with null for left and right
    }

    // Check if this HuffmanTree is equivalent to another HuffmanTree
    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;  // Same object reference
        }
    
        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;  // Null or different class
        }
    
        HuffmanTree otherTree = (HuffmanTree) otherObj;

        // Compare symbols considering possible nulls
        if (this.symbol == null) {
            if (otherTree.symbol != null) {
                return false;
            }
        } 
        
        else if (!this.symbol.equals(otherTree.symbol)) {
            return false;
        }
    

        // Compare left children
        if (this.left == null) {
            if (otherTree.left != null) {
                return false;
            }
        } 
        
        else if (!this.left.equals(otherTree.left)) {
            return false;
        }
    

        // Compare right children
        if (this.right == null) {
            if (otherTree.right != null) {
                return false;
            }
        } 
        
        else if (!this.right.equals(otherTree.right)) {
            return false;
        }
    
        return true; 
    }
    
    // Return a string representation of this HuffmanTree
    @Override
    public String toString() {
        return "HuffmanTree(" + this.symbol + ", " + left + ", " + right + ")";
    }

    // Check if this HuffmanTree is a leaf
    public boolean isLeaf() {
        return left == null && right == null;
    }

    // Return the number of nodes required to represent this Huffman tree
    // Precondition: This Huffman tree is already numbered.
    public byte[] numNodesToBytes() {
        return new byte[]{(byte) (size + 1)};
    }
}
