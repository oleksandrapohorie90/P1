import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        SimpleTextCompressionAlgorithm compressor = new SimpleTextCompressionAlgorithm();
        List<String> words = compressor.getAllWords();
        //printing all words in the file to see what we have read from the file
//        for (String word : words) {
//            System.out.println("Word: " + word);
//        }

        Map<String, Integer> map = compressor.createMap(words);
        //this map
        for (String key : map.keySet()) {
            Integer value = map.get(key);
            System.out.println("Key: " + key + ", Value: " + value);
        }

        compressor.compress(map, true);
        compressor.compress(map, false);
        //compressor.decompress(map);
        try {
            compressor.compareFiles();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}