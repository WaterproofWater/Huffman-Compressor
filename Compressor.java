import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Compressor {

    // Build the frequency map
    public static HashMap<Integer, Integer> buildFrequencyMap(byte[] text) {

        HashMap<Integer, Integer> freqMap = new HashMap<>();

        for (byte b : text) {
            int key = Byte.toUnsignedInt(b); 
            // System.out.println(key);
            freqMap.put(key, freqMap.getOrDefault(key, 0) + 1);
        }

        return freqMap;
    }

    // Create a HuffmanTree for each symbol in the frequency map
    public static ArrayList<HuffmanTree> buildHuffmanTreeList(HashMap<Integer, Integer> freqDict) {

        ArrayList<HuffmanTree> huffmanTreeList = new ArrayList<>();
        
        for (Map.Entry<Integer, Integer> entry : freqDict.entrySet()) {
            Integer symbol = entry.getKey();
            Integer freq = entry.getValue();
            HuffmanTree treeItem = new HuffmanTree(symbol, freq);
            huffmanTreeList.add(treeItem);
        }

        return huffmanTreeList;

    }

    // Merge the Huffman trees into a final form
    public static HuffmanTree HuffmanTreeMerger(ArrayList<HuffmanTree> treeList) {

        while (treeList.size() != 1) {
            HuffmanTree smallest = null;
            HuffmanTree secondSmallest = null;

            for (HuffmanTree tree : treeList) {
                if (smallest == null || tree.size < smallest.size) {
                    secondSmallest = smallest;
                    smallest = tree;
                } 

                else if (secondSmallest == null || tree.size < secondSmallest.size) {
                    secondSmallest = tree;
                }
            }

            treeList.remove(smallest);
            treeList.remove(secondSmallest);

            HuffmanTree mergedTree = new HuffmanTree(null, smallest, secondSmallest, smallest.size + secondSmallest.size);
            treeList.add(mergedTree);
        }

        return treeList.get(0);
    }

    // Encodes all symbols in the final Huffman tree into their respective codes
    public static HashMap<Integer, String> HuffmanEncoder(HuffmanTree tree) {
        HashMap<Integer, String> codeMap = new HashMap<>();

        if (tree.left != null && !tree.left.isLeaf()) {
            HashMap<Integer, String> leftCodes = HuffmanEncoder(tree.left);

            for (HashMap.Entry<Integer, String> entry : leftCodes.entrySet()) {
                codeMap.put(entry.getKey(), "0" + entry.getValue());
            }

        } 

        else if (tree.left != null && tree.left.isLeaf()) {
            codeMap.put(tree.left.symbol, "0");
        }

        if (tree.right != null && !tree.right.isLeaf()) {
            HashMap<Integer, String> rightCodes = HuffmanEncoder(tree.right);

            for (HashMap.Entry<Integer, String> entry : rightCodes.entrySet()) {
                codeMap.put(entry.getKey(), "1" + entry.getValue());
            }

        } 
        
        else if (tree.right != null && tree.right.isLeaf()) {
            codeMap.put(tree.right.symbol, "1");
        }

        return codeMap;
    }

    // Compress the bytes in the original file using the codes given by the final Huffman tree
    public static byte[] compressBytes(byte[] text, Map<Integer, String> codes) {
        StringBuilder newBits = new StringBuilder();
        for (byte b : text) {
            newBits.append(codes.get((int) b & 0xFF));
        }

        // Padding the bit string to be a multiple of 8 bits
        int paddingLength = 8 - (newBits.length() % 8);
        if (paddingLength != 8) {
            newBits.append("0".repeat(paddingLength));
        }

        int numBytes = newBits.length() / 8;
        byte[] result = new byte[numBytes];

        for (int i = 0; i < numBytes; i++) {
            String byteString = newBits.substring(i * 8, (i + 1) * 8);
            result[i] = (byte) Integer.parseInt(byteString, 2);
        }

        return result;
    }

    // Helper function to display bytes in binary form for debugging
    public static String byteToBits(byte b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }

    // Serialize the Huffman Tree to a byte array
    public static byte[] serializeHuffmanTree(HuffmanTree tree) {
        StringBuilder serializedTree = new StringBuilder();
        serializeHelper(tree, serializedTree);
        
        return serializedTree.toString().getBytes();
    }

    // Helper function for tree serialization (preorder traversal)
    private static void serializeHelper(HuffmanTree tree, StringBuilder serializedTree) {
        if (tree == null) {
            serializedTree.append("0");  
            return;
        }

        serializedTree.append("1");  
        if (tree.isLeaf()) {
            serializedTree.append((char) tree.symbol.intValue());  
        }

        serializeHelper(tree.left, serializedTree);
        serializeHelper(tree.right, serializedTree);
    }

    // Write the Huffman tree and compressed bytes to a file
    public static void writeCompressedToFile(String fileName, HuffmanTree tree, byte[] compressedData) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            byte[] serializedTree = serializeHuffmanTree(tree);
            fos.write(serializedTree);
            fos.write("\n".getBytes());
            fos.write(compressedData);
        } 
        
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Convert all symbols in a file to its byte form
    public static byte[] fileToByte(String fileName) {
        Path filePath = Paths.get(fileName);

        try {
            byte[] fileBytes = Files.readAllBytes(filePath);
            return fileBytes;
        }

        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String inputFileName = "Homer-Iliad.txt";

        byte[] fileBytes = fileToByte(inputFileName);
        if (fileBytes == null) {
            System.out.println("Error: File does not exist or failed to be read.");
            return;
        }

        // Compression Logic
        HashMap<Integer, Integer> frequencyMap = buildFrequencyMap(fileBytes);
        ArrayList<HuffmanTree> huffmanTreeList = buildHuffmanTreeList(frequencyMap);
        HuffmanTree finalTree = HuffmanTreeMerger(huffmanTreeList);
        HashMap<Integer, String> huffmanCodes = HuffmanEncoder(finalTree);
        byte[] compressedData = compressBytes(fileBytes, huffmanCodes);
        String outputFileName = inputFileName + "_compressed.huff";

        writeCompressedToFile(outputFileName, finalTree, compressedData);
        System.out.println("Compression completed. Compressed file saved as: " + outputFileName);
    }
}

