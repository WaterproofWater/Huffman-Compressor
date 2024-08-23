import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class Compressor {

    // Function to build the frequency dictionary
    public static HashMap<Integer, Integer> buildFrequencyDict(byte[] text) {
        HashMap<Integer, Integer> freqMap = new HashMap<>();
        for (byte b : text) {
            int key = Byte.toUnsignedInt(b);  // Convert byte to unsigned int
            // System.out.println(key);
            freqMap.put(key, freqMap.getOrDefault(key, 0) + 1);
        }
        return freqMap;
    }

    public static HuffmanTree buildHuffmanTree(HashMap<Integer, Integer> freqDict) {
        if (freqDict.size() == 1) {
            Integer symbol = freqDict.keySet().iterator().next();
            return new HuffmanTree(symbol, new HuffmanTree(symbol), null);
        }

        if (freqDict.isEmpty()) {
            return new HuffmanTree(null);
        }

        List<HuffmanTree> store = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : freqDict.entrySet()) {
            HuffmanTree tree = new HuffmanTree(entry.getKey());
            tree.number = entry.getValue();
            store.add(tree);
        }

        while (store.size() > 1) {
            store.sort(Comparator.comparingInt(t -> t.number));
            HuffmanTree lowest = store.remove(0);
            HuffmanTree secondLowest = store.remove(0);

            HuffmanTree tree = new HuffmanTree(null, lowest, secondLowest);
            tree.number = lowest.number + secondLowest.number;
            store.add(tree);
        }

        HuffmanTree resultTree = store.get(0);
        List<HuffmanTree> cleanser = treeToList(resultTree);

        for (HuffmanTree tree : cleanser) {
            tree.number = 0;
        }

        return resultTree;
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
       
        HashMap<Integer, Integer> b = buildFrequencyDict(fileInByte);
        System.out.println(b);
        HuffmanTree testTree = buildHuffmanTree(b);
        System.out.println(testTree);
       
    }
}

