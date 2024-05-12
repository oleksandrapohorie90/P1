import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleTextCompressionAlgorithm {

    private static final String INPUT_FILE_PATH = "src/files/input.txt";
    private static final String READABLE_FILE_PATH = "src/files/readable.txt";
    private static final String OUTPUT_FILE_PATH = "src/files/output.sc";

    public List<String> getAllWords() {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                for (var word : line.split("\s")) {
                    words.add(word);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } finally {
            return words;
        }
    }

    public Map<String, Integer> createMap(List<String> words) {
        Map<String, Integer> map = new HashMap<>();
        int count = 0;

        for (String word : words) {
            if (!map.containsKey(word)) {
                map.put(word, count);
            }
            count++;
        }
        return map;
    }

    public void compress(Map<String, Integer> map, boolean compress) {
        BufferedWriter writer = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(compress ? INPUT_FILE_PATH : OUTPUT_FILE_PATH));
            writer = new BufferedWriter(new FileWriter(compress ? OUTPUT_FILE_PATH : READABLE_FILE_PATH));
            String line;

            while ((line = reader.readLine()) != null) {
                String newLine = "";

                for (var word : line.split("\s")) {
                    if (compress)
                        newLine += map.get(word) + " ";
                    else
                        newLine += getKey(map, Integer.valueOf(word)) + " ";
                }

                newLine = newLine.substring(0, newLine.length() - 1);
                writer.write(newLine);

                if (reader.ready())
                    writer.newLine();
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

    }

    public void decompress(Map<String, Integer> map) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(OUTPUT_FILE_PATH));
            BufferedWriter writer = new BufferedWriter(new FileWriter(READABLE_FILE_PATH));
            String line;

            while ((line = reader.readLine()) != null) {
                String newLine = "";

                for (var word : line.split("\s")) {
                    newLine += getKey(map, Integer.valueOf(word)) + " ";
                }

                newLine = newLine.substring(0, newLine.length() - 1);
                writer.write(newLine);

                if (reader.ready())
                    writer.newLine();
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public String getKey(Map<String, Integer> map, int value) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {

            if (entry.getValue() == value) {
                return entry.getKey();
            }
        }

        return null;
    }

    public void compareFiles() throws IOException {
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(INPUT_FILE_PATH), StandardCharsets.UTF_8));
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(READABLE_FILE_PATH), StandardCharsets.UTF_8));

        String line1, line2;
        boolean differences = false; // Flag to track differences

        // Read and compare each line from both files
        while ((line1 = reader1.readLine()) != null && (line2 = reader2.readLine()) != null) {
            if (!line1.equals(line2)) { // If lines do not match
                differences = true;
                break; // Break the loop on first difference
            }
        }

        // Check for any remaining lines in either file
        if (differences || reader1.readLine() != null || reader2.readLine() != null) {
            System.out.println("Files are different.");
        } else {
            System.out.println("Files are identical.");
        }

        reader1.close(); // Close the first reader
        reader2.close(); // Close the second reader
    }
}
