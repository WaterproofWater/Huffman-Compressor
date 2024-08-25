import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Decompressor {

    private static int position = 0;

    // Deserialize the Huffman tree from the serialized string
    public static HuffmanTree deserializeHuffmanTree(String serializedTree) {
        if (position >= serializedTree.length()) {
            return null;
        }

        char nodeType = serializedTree.charAt(position);
        position++;

        if (nodeType == 'L') { // Leaf node: extract the symbol
            StringBuilder symbolBuilder = new StringBuilder();

            while (position < serializedTree.length() && 
                  (Character.isDigit(serializedTree.charAt(position)))) {
                symbolBuilder.append(serializedTree.charAt(position));
                position++;
            }

            int symbol = Integer.parseInt(symbolBuilder.toString());
            return new HuffmanTree(symbol, 1); // Leaf nodes have size 1
        } 
        
        else if (nodeType == 'I') { // Internal node: recursively construct the left and right children
            HuffmanTree left = deserializeHuffmanTree(serializedTree);
            HuffmanTree right = deserializeHuffmanTree(serializedTree);

            int size = 1 + (left != null ? left.size : 0) + (right != null ? right.size : 0); // Size of the internal node
            return new HuffmanTree(null, left, right, size);
        }

        throw new IllegalArgumentException("Invalid serialized tree format at position: " + position);
    }

    // Convert a byte array to an int
    public static int bytesToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
    }

    // Main method for testing deserialization
    public static void main(String[] args) {
        // Scanner scanner = new Scanner(System.in);

        // System.out.print("Enter the name of the compressed file to decompress: ");
        String fileName = "Homer-Iliad.txt_compressed.huff";
        //scanner.nextLine();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {
            int treeLength = dis.readInt();
            System.out.println("Length of serialized Huffman tree: " + treeLength);

            byte[] treeBytes = new byte[treeLength];
            dis.readFully(treeBytes);

            String serializedTree = new String(treeBytes);
            System.out.println("Serialized Huffman tree: " + serializedTree);

            HuffmanTree test = deserializeHuffmanTree(serializedTree);
            System.out.println(test);
        } 
        
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
