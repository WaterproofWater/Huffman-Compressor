import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class Compressor {

    // Build the frequency map
    public static HashMap<Integer, Integer> buildFrequencyMap(byte[] text) {
        HashMap<Integer, Integer> freqMap = new HashMap<>();
        for (byte b : text) {
            int key = Byte.toUnsignedInt(b);  // Convert byte to unsigned int
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




















    public static byte[] fileToByte(String fileName) {
        Path filePath = Paths.get(fileName);

        try {
            byte[] fileBytes = Files.readAllBytes(filePath);

            // for (byte b : fileBytes) {
            //     System.out.println("Byte value: " + b);
            // }

            return fileBytes;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
       byte[] fileInByte = fileToByte("example.txt");

       if (fileInByte == null || fileInByte.length == 0) {
           System.out.println("File is empty or there was an error reading the file.");
           return;
       } 
       
        HashMap<Integer, Integer> b = buildFrequencyMap(fileInByte);
        System.out.println(b);
        ArrayList<HuffmanTree> testTreeList = buildHuffmanTreeList(b);
        System.out.println(testTreeList);

        HuffmanTree finalTree = HuffmanTreeMerger(testTreeList);
        System.out.println(finalTree);
       
    }
}

