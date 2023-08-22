package codSoftPackage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class WordCounter {
	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter 't' to input directly text or 'f' to provide a file: ");
        String inputType = sc.nextLine().trim().toLowerCase();

        if (inputType.equals("t")) {
            System.out.print("Enter the text: ");
            String userinput = sc.nextLine();
            wordsCount(userinput);
        } else if (inputType.equals("f")) {
            System.out.print("Enter the file name: ");
            String fileName = sc.nextLine();
            try {
                String fileContent = readFile(fileName);
                wordsCount(fileContent);
            } catch (IOException e) {
                System.out.println("File not found.");
            }
        } else {
            System.out.println("Invalid type of Input.");
        }

        sc.close();
    }

    public static void wordsCount(String text) {
        String[] wordsArray = text.toLowerCase().split("\\W+");
        int wordCount = wordsArray.length;

        System.out.println("Count of Total words: " + wordCount);

        Map<String, Integer> wordFrequency = new HashMap<>();
        for (String word : wordsArray) {
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }

        int unique = wordFrequency.size();
        System.out.println("Count of Unique words: " + unique);

       System.out.println("Count of Common words:");
        wordFrequency.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(10)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }

    public static String readFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}
