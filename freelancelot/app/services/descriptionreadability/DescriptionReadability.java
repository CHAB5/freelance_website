package services.descriptionreadability;

import java.util.*;
import java.util.stream.Stream;

/**
 *  DescriptionReadability class provides methods for computing flesch readbility indices, and auxiliary methods <br>
 *  Methods are deliberately static, as they are intended to be used with streams, as pure stateless functions
 *  for counting sentences, words, syllables according to the rules specified in the following document:
 *  Reference : http://users.csc.calpoly.edu/~jdalbey/305/Projects/FleschReadabilityProject.html
 *
 * @author Shubham Punekar
 */
public class DescriptionReadability {

    /**
     * Static method to count the number of sentences in the given text <br>
     * Periods, exclamation points, question marks, colons and semicolons serve as sentence delimiters.
     * @param text Input string
     * @return Integer wrapped in optional for number of sentences in the input text
     */
    public static Optional<Integer> countSentences(String text) {
        List<String> sentences = Arrays.asList(text.split("[.!?:;]"));
        return sentences
                .stream()
                .map(sentence -> {
                    // Handle the case for empty/blank string string
                    if (sentence.isBlank())
                        return 0;
                    else return 1;
                })
                .reduce(Integer::sum);
    }

    /**
     * Static method to remove punctuation from the given text <br>
     * Non word characters (not in "[A-Za-z_]") are considered as puncutation
     * @param text Input string
     * @return String with punctuation removed
     */
    public static String removePunctuation(String text) {
        // Replace anything that is not a word character [A-Za-z0-9_]a whitespace character [ \t\n\r\f\v] with a space
        text = text.replaceAll("[^\\w\\s]", "");
        // Note that this doesn't account for consecutive spaces (i.e. "a b" -> ["a", "b"] but "a   b" -> ["a", "", "",  "b"]
        // But this can be handled while counting words, by ignoring empty strings
        return text;

    }

    /**
     * Static method to count number of words in the given text <br>
     * Words are delimited by white space characters ( space, \n, \r, \t etc)
     * @param text Input string
     * @return Integer wrapped in Optional for count of words
     */
    public static Optional<Integer> countWords(String text) {
        String cleanedText = removePunctuation(text);
        // Split word on whitespace characters ( space, \n, \r, \t etc)
        return Arrays.stream(cleanedText.split("\\s+"))
                .map(word -> {
                    if (word.isBlank()) return 0;
                    else return 1;
                })
                .reduce(Integer::sum);
    }

    /**
     * Static method to count syllables in the given word <br>
     * Syllables are counted according to following rules: <br>
     * Each vowel in a word is considered one syllable subject to: <br>
     * :: words of three letters or shorter count as single syllables; <br>
     * :: -es, -ed and -e (except -le) endings are ignored; <br>
     * :: consecutive vowels count as one syllable. <br>
     * @param word Input string
     * @return Integer wrapped in Optional for count of syllables
     */
    public static int countSyllablesInWord(String word) {
        // words of three letters or shorter count as single syllables
        if (word.length() <= 3) {
            return 1;
        }

        word = word.toLowerCase();
        HashSet<String> vowels = new HashSet<String>();
        Collections.addAll(vowels, "a", "e", "i", "o", "u", "y");

        // each vowel in a word is considered one syllable subject to 2 conditions
        int count = 0;
        char[] wordCharArray = word.toCharArray();
        for (char c : wordCharArray) if (vowels.contains(Character.toString(c))) count++;

        // condition 1: consecutive vowels count as one syllable
        for (int i = 0; i < wordCharArray.length - 1; i++) {
            if (vowels.contains(Character.toString(wordCharArray[i]))
                    && vowels.contains(Character.toString(wordCharArray[i + 1])))
                count--;
        }

        // condition 2: -es, -ed and -e (except -le) endings are ignored;
        if (word.endsWith("es") || word.endsWith("ed") || word.endsWith("e")) count--;
        if (word.endsWith("le")) count++;

        return count;
    }

    /**
     * Static method to count syllables in the given text <br>
     * @param text Input string
     * @return Integer wrapped in Optional for count of syllables
     * @see DescriptionReadability#countSyllablesInWord(String)
     */
    public static Optional<Integer> countSyllables(String text) {
        // terminal punctuation has to be removed, otherwise str.endswith() won't work if words end with punctuation
        text = text.replaceAll("[.!?:;]$", "");
        // apostrophe's have to be removed as well, as cache's won't work right.
        text = text.replaceAll("'", "");
        String[] words = text.split("\\s+");

        return Stream.of(words)
                .map(DescriptionReadability::countSyllablesInWord)
                .reduce(Integer::sum);
    }

    /**
     * Static method to compute Flesch Readability Index <br>
     * Flesch Index = 206.835 - 84.6 * (Syllables / Words) - 1.015 * (Words / Sentences)
     * @param text Input string
     * @return Double value for flesh readability index
     * @see DescriptionReadability#computeFleschKincaidGradeLevel(String) 
     */
    public static double computeFleschReadabilityIndex(String text) {
        try {
            return
                    206.835
                            - (84.6 * (float) countSyllables(text).orElseThrow(() -> new NoSuchElementException("Missing syllables")) / countWords(text).orElseThrow(() -> new NoSuchElementException("Missing words")))
                            - (1.015 * (float) countWords(text).orElseThrow(() -> new NoSuchElementException("Missing words")) / countSentences(text).orElseThrow(() -> new NoSuchElementException("Missing sentences")));
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    /**
     * Static method to compute Flesch Kincaid Grade Level <br>
     * Flesch Kincaid Grade Level =  0.39 * (Words / Sentences) + 11.8 * (Syllables / Words) * - 15.59
     * @param text Input string
     * @return Double value for flesch kincaid grade level
     * @see DescriptionReadability#computeFleschReadabilityIndex(String)
     */
    public static double computeFleschKincaidGradeLevel(String text) {
        try {
            return
                    0.39 * ((float) countWords(text).orElseThrow(() -> new NoSuchElementException("Missing words")) / countSentences(text).orElseThrow(() -> new NoSuchElementException("Missing sentences")))
                            + 11.8 * ((float) countSyllables(text).orElseThrow(() -> new NoSuchElementException("Missing syllables")) / countWords(text).orElseThrow(() -> new NoSuchElementException("Missing words")))
                            - 15.59;
        } catch (NoSuchElementException e) {
            return 0;
        }
    }
}
