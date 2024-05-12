import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        SimpleTextCompressionAlgorithm compressor = new SimpleTextCompressionAlgorithm ();
        List<String> words = compressor.getAllWords();
        Map<String, Integer> map = compressor.createMap(words);

        for (String word : words) {
            System.out.println("Word: " + word);
        }

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