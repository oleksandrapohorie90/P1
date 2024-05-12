import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

// Main class for text compression and decompression algorithm
public class SimpleTextCompressionAlgorithmOld {
    // Constants for file paths
    private static final String INPUT_FILE = "C:\\Users\\AlexaSky\\IdeaProjects\\P1\\src\\input.txt";
    private static final String COMPRESSED_FILE = "output.sc";
    private static final String DECOMPRESSED_FILE = "readable.txt";

    // Main method to control the flow of compression, decompression, and comparison
    public static void main(String[] args) throws IOException {
        compressFile(INPUT_FILE, COMPRESSED_FILE);
        decompressFile(COMPRESSED_FILE, DECOMPRESSED_FILE);
        compareFiles(INPUT_FILE, DECOMPRESSED_FILE);
    }

    // Method to compress the input text file
    public static void compressFile(String inputFile, String compressedFile) throws IOException {
        // Setting up reader and writer with UTF-8 encoding to handle text correctly
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(compressedFile), StandardCharsets.UTF_8));

        // Dictionary to map words to codes
        Map<String, Integer> dictionary = new HashMap<>();
        int code = 129; // Starting code value for words

        // Reading the input file line by line
        String line;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\\s+"); // Split line into words by whitespace
            for (String word : words) {
                if (!dictionary.containsKey(word)) { // If word is not already in dictionary
                    dictionary.put(word, code++); // Add word to dictionary with next code
                }
                writer.write(dictionary.get(word) + " "); // Write the code to the compressed file
            }
            writer.write(System.lineSeparator()); // Add a newline in the compressed file for each new line read
        }

        writer.close(); // Close the writer to flush and release resources
        reader.close(); // Close the reader to release resources

        saveDictionary(dictionary); // Save the dictionary for decompression
    }

    // Method to decompress the previously compressed file
    public static void decompressFile(String compressedFile, String decompressedFile) throws IOException {
        // Setting up reader and writer with UTF-8 encoding
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(compressedFile), StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(decompressedFile), StandardCharsets.UTF_8));

        // Load the dictionary used during compression
        Map<Integer, String> dictionary = loadDictionary();

        // Read the compressed file line by line
        String line;
        while ((line = reader.readLine()) != null) {
            String[] codes = line.trim().split("\\s+"); // Split line into individual codes
            boolean first = true; // Flag to manage spaces between words
            for (String code : codes) {
                if (!first) {
                    writer.write(" "); // Write a space before each word except the first
                }
                writer.write(dictionary.get(Integer.parseInt(code))); // Decode the code and write the word
                first = false; // After the first word, set flag to false
            }
            writer.write(System.lineSeparator()); // Write a newline after each line
        }

        writer.close(); // Close the writer to flush and release resources
        reader.close(); // Close the reader to release resources
    }

    // Method to save the dictionary object to a file
    private static void saveDictionary(Map<String, Integer> dictionary) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dictionary.bin"));
        out.writeObject(dictionary); // Serialize and write the dictionary to a binary file
        out.close(); // Close the output stream
    }

    // Method to load the dictionary object from a file
    private static Map<Integer, String> loadDictionary() throws IOException {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("dictionary.bin"));
            Map<String, Integer> dictionary = (Map<String, Integer>) in.readObject(); // Deserialize the dictionary object
            Map<Integer, String> reverseDictionary = new HashMap<>();
            for (Map.Entry<String, Integer> entry : dictionary.entrySet()) {
                reverseDictionary.put(entry.getValue(), entry.getKey()); // Create reverse mapping
            }
            in.close(); // Close the input stream
            return reverseDictionary;
        } catch (ClassNotFoundException e) {
            throw new IOException("Dictionary file corrupted.", e);
        }
    }

    // Method to compare the original and decompressed files to verify integrity
    public static void compareFiles(String file1, String file2) throws IOException {
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1), StandardCharsets.UTF_8));
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2), StandardCharsets.UTF_8));

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
