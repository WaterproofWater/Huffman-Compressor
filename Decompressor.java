import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Decompressor {

    private static int position = 0;

    // Decompress the file using the deserialized Huffman tree
    public static String decompress(String inputFileName, HuffmanTree deserializedRoot, int serializedTreeLength) {
        String originalFileName = inputFileName.replace("_compressed.huff", "");
        String originalExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String outputFileName = originalFileName + "_decompressed" + originalExtension;

        try {
            FileInputStream fis = new FileInputStream(inputFileName);
            DataInputStream dis = new DataInputStream(fis);
            FileOutputStream fos = new FileOutputStream(outputFileName);

            fis.skip(serializedTreeLength + 4);

            int contentLength = dis.readInt();

            byte[] compressedData = dis.readAllBytes();
            String bitString = byteArrayToBitString(compressedData);

            HuffmanTree currentNode = deserializedRoot;
            int decompressedBytes = 0;

            for (int i = 0; (i < bitString.length()) && (decompressedBytes < contentLength); i++) {
                char bit = bitString.charAt(i);
                currentNode = (bit == '1') ? currentNode.right : currentNode.left;

                if (currentNode.isLeaf()) {
                    fos.write(currentNode.symbol);
                    currentNode = deserializedRoot;
                    decompressedBytes++;
                }
            }

            dis.close();
            fos.close();
            fis.close();

        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return outputFileName;
    }

    // Convert a byte array to a bit string
    private static String byteArrayToBitString(byte[] byteArray) {
        StringBuilder bitString = new StringBuilder();

        for (byte b : byteArray) {
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            bitString.append(binaryString);
        }

        return bitString.toString();
    }


    // Deserialize the Huffman tree from the serialized string
    public static HuffmanTree deserializeHuffmanTree(String serializedTree) {
        if (position >= serializedTree.length()) {
            return null;
        }

        char nodeType = serializedTree.charAt(position);
        position++;

        if (nodeType == 'L') { // Leaf node: extract the symbol
            StringBuilder symbolBuilder = new StringBuilder();

            while ((position < serializedTree.length()) && (Character.isDigit(serializedTree.charAt(position)))) {
                symbolBuilder.append(serializedTree.charAt(position));
                position++;
            }

            int symbol = Integer.parseInt(symbolBuilder.toString());

            return new HuffmanTree(symbol, 1);
        }

        else if (nodeType == 'I') { // Internal node: recursively construct the left and right children
            HuffmanTree left = deserializeHuffmanTree(serializedTree);
            HuffmanTree right = deserializeHuffmanTree(serializedTree);

            int size = 1;
            if (left != null) {
                size += left.size;
            }

            if (right != null) {
                size += right.size;
            }

            return new HuffmanTree(null, left, right, size);
        }

        throw new IllegalArgumentException("Invalid serialized tree format at position: " + position);
    }

    // Convert a byte array to an int (credit: https://www.baeldung.com/java-byte-array-to-number#:~:text=intBitsToFloat()%20method%3A,intBitsToFloat(intValue)%3B)
    public static int bytesToInt(byte[] bytes) {
        int value = 0;

        for (byte b : bytes) {
            value = (value << 8) + (b & 0xFF);
        }

        return value;
    }

    // Main method for testing deserialization
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the file to decompress: ");
        String fileName = scanner.nextLine();

        try {
            FileInputStream targetFile =  new FileInputStream(fileName);
            DataInputStream dis = new DataInputStream(targetFile);
            int treeLength = dis.readInt();

            byte[] treeBytes = new byte[treeLength];
            dis.readFully(treeBytes);

            String serializedTree = new String(treeBytes);
            HuffmanTree decodedTree = deserializeHuffmanTree(serializedTree);

            String outputFileName = decompress(fileName, decodedTree, serializedTree.length());

            dis.close();
            targetFile.close();

            System.out.println("Decompression completed. Decompressed file saved as: " + outputFileName);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
