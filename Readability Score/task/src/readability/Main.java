package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        // Main variables
        String text = readFileAsString(args[0]);
        Scanner scanner = new Scanner(System.in);
        final char[] vowels = {'a', 'e', 'i', 'o', 'u', 'y', 'A', 'E', 'I', 'O', 'U', 'Y'};
        int sentencesNum = text.split("[.!?]").length;
        String[] words = text.replaceAll("\\p{Punct}", "").split(" ");
        int charactersNum = text.replaceAll("\\s+", "").length();
        int syllablesNum = 0;
        int polySyllablesNum = 0;

        // Count syllables and polysyllables
        for (String word : words) {
            int syllablesNumPerWord = 0;
            word = word.replaceAll("e$", "").replaceAll("[aeiouyAEIOUY]{2,}", "a");
            for (char letter : word.toCharArray()) {
                if (Arrays.toString(vowels).contains(Character.toString(letter))) {
                    syllablesNumPerWord++;
                }
            }
            polySyllablesNum += syllablesNumPerWord > 2 ? 1 : 0;
            syllablesNum += syllablesNumPerWord <= 0 ? 1 : syllablesNumPerWord;
        }

        // Calculate indexes
        double ariScore = 4.71 * ((double) charactersNum / words.length) + 0.5
                * ((double) words.length / sentencesNum) - 21.43;
        double fkScore = 0.39 * ((double) words.length / sentencesNum) + 11.8
                * ((double) syllablesNum / words.length) - 15.59;
        double smogIndex = 1.043 * Math.sqrt((double) polySyllablesNum * (30 / (double) sentencesNum)) + 3.1291;
        double clIndex = 0.0588 * (((double) charactersNum / words.length) * 100) - 0.296
                * (((double) sentencesNum / words.length) * 100) - 15.8;

        //Results
        int[] correspondingAges = {(int) Math.round(ariScore) + 6, (int) Math.round(fkScore) + 6,
                (int) Math.round(smogIndex) + 6, (int) Math.round(clIndex) + 6};
        String[] results = {
                "Automated Readability Index: "
                        + Math.round(ariScore * 100.0) / 100.0 + " (about " + correspondingAges[0] + "-year-olds).",
                "Flesch-Kincaid readability tests: "
                        + Math.round(fkScore * 100.0) / 100.0 + " (about " + correspondingAges[1] + "-year-olds).",
                "Simple Measure of Gobbledygook: "
                        + Math.round(smogIndex * 100.0) / 100.0 + " (about " + correspondingAges[2] + "-year-olds).",
                "Coleman-Liau index: "
                        + Math.round(clIndex * 100.0) / 100.0 + " (about " + correspondingAges[3] + "-year-olds)."};

        // Show results
        System.out.println("The text is:\n" + text + "\n\n" + "Words: " + words.length + "\n" + "Sentences: "
                + sentencesNum + "\n" + "Characters: " + charactersNum + "\n"
                + "Syllables: " + syllablesNum + "\n"
                + "Polysyllables: " + polySyllablesNum + "\n"
                + "Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String userInput = scanner.nextLine();
        System.out.println();
        switch (userInput) {
            case "ARI" -> System.out.println("\n" + results[0]);
            case "FK" -> System.out.println("\n" + results[1]);
            case "SMOG" -> System.out.println("\n" + results[2]);
            case "CL" -> System.out.println("\n" + results[3]);
            case "all" -> {
                for (String result : results) {
                    System.out.println(result);
                }
            }
        }
        System.out.println("\nThis text should be understood in average by "
                + Arrays.stream(correspondingAges).average().orElse(-1) + "-year-olds.");
    }

    public static String readFileAsString(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
