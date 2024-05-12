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

    /** this method is just to get all the files from the INPUT_FILE
    RETURN List of all words
     */
    public List<String> getAllWords() {
        //list for words found in the file to return
        List<String> words = new ArrayList<>();
        //BufferedReader allows to read a file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
            String line; //to store the first line of the file and so on
            //if line is null that means we are at the end of the file, if not null we continue reading line by line
            while ((line = reader.readLine()) != null) {
                for (var word : line.split("\s")) {
                    words.add(word);//splitting by space in above line and adding each word into List words
                }
            }
            //to close the file, we opened the file by creating an object, line 21
            reader.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        } finally {
            return words;
        }
    }

    /**
     this method assings words to its numbers by creating a map from a list of words where each word is assigned a unique integer;
     it iterates over the list of words, and for each word not already in the map, adds the word with an incrementing integer value as its mapped value;
     then map is returned, with words as keys and their corresponding integers as values;
     */
    public Map<String, Integer> createMap(List<String> words) {
        Map<String, Integer> map = new HashMap<>();
        int count = 0;
        //get the 1st word and put in the map as a key
        //if map contains the key - ignore
        for (String word : words) {
            if (!map.containsKey(word)) {
                map.put(word, count);
            }
            count++;
        }
        return map;
    }

    /** create a file using numbers instead of words
     this method either compresses or decompresses text based on the boolean parameter compress; for compression, it reads from INPUT_FILE_PATH and writes to OUTPUT_FILE_PATH, replacing each word with its corresponding integer from the map;
     for decompression, it reads from OUTPUT_FILE_PATH and writes to READABLE_FILE_PATH, replacing each integer with its corresponding word using the getKey method;
     it writes each transformed line to the appropriate file, ensuring to add new lines as needed.
     */
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

    /**
     this method is for decompressing, it reads integers from OUTPUT_FILE_PATH and writes the corresponding words to READABLE_FILE_PATH;
     it performs similar operations as the decompression part of the compress method, but it is a standalone method;
     */
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

    /**
     this utility method retrieves a key (word) from the map based on a given value (integer);
     it iterates over the entries in the map and returns the key where the value matches the given integer
     */
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
