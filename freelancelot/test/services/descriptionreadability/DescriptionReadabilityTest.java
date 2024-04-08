package services.descriptionreadability;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import static services.descriptionreadability.DescriptionReadability.*;

/**
 *  DescriptionReadabilityTest Junit tests for testing DescriptionReadability service class
 *
 * @author Shubham Punekar
 */
public class DescriptionReadabilityTest {

    /**
     * Test to exercise {@link DescriptionReadability#countSentences(String)} <br>
     * Includes edge cases for all sentence delimiters.
     * @see DescriptionReadability#countSentences(String) 
     */
    @Test
    public void testCountSentences() {
        Assertions.assertEquals(Optional.of(1), countSentences("abc"));
        Assertions.assertEquals(Optional.of(0), countSentences(""));
        Assertions.assertEquals(Optional.of(1), countSentences("abc def"));
        Assertions.assertEquals(Optional.of(1), countSentences("Abc def."));
        Assertions.assertEquals(Optional.of(1), countSentences("Abc def?"));
        Assertions.assertEquals(Optional.of(1), countSentences("Abc def!"));
        Assertions.assertEquals(Optional.of(1), countSentences("Abc def:"));
        Assertions.assertEquals(Optional.of(1), countSentences("Abc def;"));
        Assertions.assertEquals(Optional.of(3), countSentences("Abc def. RST xyz? WWW!"));
    }

    /**
     * Test to exercise {@link DescriptionReadability#removePunctuation(String)} <br>
     * Includes edge cases for all non word characters treated as punctuation.
     * @see DescriptionReadability#removePunctuation(String) 
     */
    @Test
    public void testRemovePunctuation() {
        Assertions.assertEquals("abcdxyz", removePunctuation("abcd,?xyz!:;"));
        Assertions.assertEquals("abcd pqrs xyz", removePunctuation("ab###cd p@q^r$s* x!@#$y,.><?:'z+-()"));
    }

    /**
     * Test to exercise {@link DescriptionReadability#countWords(String)} <br>
     * Includes edge cases for all whitespace characters treated as sentence delimiters.
     * @see DescriptionReadability#countWords(String) 
     */
    @Test
    public void testCountWords() {
        Assertions.assertEquals(Optional.of(0), countWords(""));
        Assertions.assertEquals(Optional.of(1), countWords("ABC"));
        Assertions.assertEquals(Optional.of(1), countWords("abc"));
        Assertions.assertEquals(Optional.of(1), countWords("123"));
        Assertions.assertEquals(Optional.of(2), countWords("abc def"));
        Assertions.assertEquals(Optional.of(2), countWords("abc\ndef"));
        Assertions.assertEquals(Optional.of(2), countWords("abc\tdef"));
    }

    /**
     * Test to exercise {@link DescriptionReadability#countSyllablesInWord(String)} <br>
     * Includes edge cases for according to the rules stated in {@link DescriptionReadability#countSyllablesInWord(String)} method
     * @see DescriptionReadability#countSyllablesInWord(String) 
     */
    @Test
    public void testCountSyllablesInWord() {
        Assertions.assertEquals(1, countSyllablesInWord("the"));
        Assertions.assertEquals(1, countSyllablesInWord("an"));
        Assertions.assertEquals(1, countSyllablesInWord("abbb"));
        Assertions.assertEquals(1, countSyllablesInWord("bebb"));
        Assertions.assertEquals(1, countSyllablesInWord("bbib"));
        Assertions.assertEquals(1, countSyllablesInWord("bbbo"));
        Assertions.assertEquals(1, countSyllablesInWord("bubb"));
        Assertions.assertEquals(1, countSyllablesInWord("bbby"));
        Assertions.assertEquals(2, countSyllablesInWord("abeb"));
        Assertions.assertEquals(1, countSyllablesInWord("board"));
        Assertions.assertEquals(1, countSyllablesInWord("baeb"));
        Assertions.assertEquals(2, countSyllablesInWord("bubbly"));
        Assertions.assertEquals(1, countSyllablesInWord("caches"));
        Assertions.assertEquals(1, countSyllablesInWord("cache"));
        Assertions.assertEquals(1, countSyllablesInWord("tested"));
        Assertions.assertEquals(2, countSyllablesInWord("thistle"));
        Assertions.assertEquals(2, countSyllablesInWord("whistle"));
    }

    /**
     * Test to exercise {@link DescriptionReadability#countSyllables(String)} <br>
     * Includes edge cases for according to the rules stated in {@link DescriptionReadability#countSyllablesInWord(String)} method
     * @see DescriptionReadability#countSyllables(String)
     */
    @Test
    public void testCountSyllables() {
        Assertions.assertEquals(Optional.of(6), countSyllables("This whistle has been tested"));
        Assertions.assertEquals(Optional.of(5), countSyllables("Cache invalidated"));
        Assertions.assertEquals(Optional.of(6), countSyllables("board is bubbly bubble"));
    }

    /**
     * Test to exercise {@link DescriptionReadability#computeFleschReadabilityIndex(String)} <br>
     * Includes edge case non-english unicode strings
     * @see DescriptionReadability#computeFleschReadabilityIndex(String)
     */
    @Test
    public void testComputeFleschReadabilityIndex() {
        String test = "This is a sentence. \n "
                + "This is an exclamation! \n"
                + "This is a question? \n"
                + "This is just a statement:  \n"
                + "This is the first clause; \n"
                + "followed by a second clause \n";
        Assertions.assertEquals(83.20083333333335, computeFleschReadabilityIndex(test));

        Assertions.assertEquals(0, computeFleschReadabilityIndex("सहायक ऑपरेटर की जरुरत है"));
    }

    /**
     * Test to exercise {@link DescriptionReadability#computeFleschKincaidGradeLevel(String)} <br>
     * Includes edge case non-english unicode strings
     * @see DescriptionReadability#computeFleschKincaidGradeLevel(String)
     */
    @Test
    public void testComputeFleschKincaidGradeLevel() {
        String test = "This is a sentence. \n "
                + "This is an exclamation! \n"
                + "This is a question? \n"
                + "This is just a statement:  \n"
                + "This is the first clause; \n"
                + "followed by a second clause \n";
        Assertions.assertEquals(2.772407355308534, computeFleschKincaidGradeLevel(test));

        Assertions.assertEquals(0, computeFleschKincaidGradeLevel("सहायक ऑपरेटर की जरुरत है"));
    }

    /**
     * Data class to create data for parameterized tests     *
     * @author Shubham Punekar
     */
    public static class TestCase {
        public String data;
        public int sentences;
        public int words;
        public int syllables;
        public double flesch_index;
        public double flesh_kincaid_grade_level;

        /**
         * Each test case contains the data, as well as the expected outputs for each of the methods in {@link DescriptionReadability}
         * @param data Input data for the test
         * @param sentences Expected number of sentences in the input
         * @param words Expected number of words in the input
         * @param syllables Expected number of syllables in the input
         * @param flesch_index Expected flesch readability index for the input
         * @param flesh_kincaid_grade_level Expected flesch kincaid grade level for the input
         */
        public TestCase(String data, int sentences, int words, int syllables, double flesch_index, double flesh_kincaid_grade_level) {
            this.data = data;
            this.sentences = sentences;
            this.words = words;
            this.syllables = syllables;
            this.flesch_index = flesch_index;
            this.flesh_kincaid_grade_level = flesh_kincaid_grade_level;
        }
    }

    /**
     * Generator method used for generating a list of test cases for parameterized tests
     * @return List of test cases to exercise all methods in {@link DescriptionReadability}
     */
    public static List<TestCase> generateTestCases() {
        List<TestCase> data = new ArrayList<>();

        // Custom test case
        data.add(
                new TestCase(
                        "This is a sentence. \n "
                                + "This is an exclamation! \n"
                                + "This is a question? \n"
                                + "This is just a statement:  \n"
                                + "This is the first clause; \n"
                                + "followed by a second clause \n",
                        6,
                        27,
                        38,
                        83.20083333333335, // 83
                        2.772407355308534
                )
        );

        // http://users.csc.calpoly.edu/~jdalbey/305/Projects/FleschReadabilityProject.html
        data.add(
                new TestCase(
                        "#$can't9* \"ain't,\" 234ABC 23abn45 @#$aba34dfs#$% @a@e@i@o@u@",
                        1,
                        6,
                        11,
                        45.64500000000001, // 46
                        8.383333802223206
                )
        );

        // https://en.wikipedia.org/wiki/Flesch%E2%80%93Kincaid_readability_tests#cite_note-10
        data.add(
                new TestCase(
                        "The cat sat on the mat.",
                        1,
                        6,
                        6,
                        116.14500000000001, // 116
                        -1.4499999999999993
                )
        );

        // https://en.wikipedia.org/wiki/Flesch%E2%80%93Kincaid_readability_tests#cite_note-10
        data.add(
                new TestCase(
                        "The Australian platypus is seemingly a hybrid of a mammal and reptilian creature.",
                        1,
                        13,
                        24,
                        37.45538461538464, // 37
                        11.264615492820742
                )
        );

        // https://en.wikipedia.org/wiki/Flesch%E2%80%93Kincaid_readability_tests#cite_note-10
        data.add(
                new TestCase(
                        "This sentence, taken as a reading passage unto itself, is being used to prove a point.",
                        1,
                        16,
                        23,
                        68.98250000000002, // 69
                        7.612500000000001
                )
        );

        // Special syllable case ?
        data.add(
                new TestCase(
                        "creature.",
                        1,
                        1,
                        2,
                        36.62000000000002, // 37
                        8.400000000000002
                )
        );

        return data;
    }

    /**
     * A parameterized test that exercises all methods from @{@link DescriptionReadability} for end-to-end tests
     * @param testCase Each test case from the generator method
     */
    @ParameterizedTest
    @MethodSource("generateTestCases")
    public void testOnData(TestCase testCase) {
        Assertions.assertEquals(
                DescriptionReadability.countSentences(testCase.data),
                Optional.of(testCase.sentences)
        );
        Assertions.assertEquals(
                DescriptionReadability.countWords(testCase.data),
                Optional.of(testCase.words)
        );
        Assertions.assertEquals(
                DescriptionReadability.countSyllables(testCase.data),
                Optional.of(testCase.syllables)
        );
        Assertions.assertEquals(
                DescriptionReadability.computeFleschReadabilityIndex(testCase.data),
                testCase.flesch_index
        );
        Assertions.assertEquals(
                DescriptionReadability.computeFleschReadabilityIndex(testCase.data),
                testCase.flesch_index
        );
        Assertions.assertEquals(
                DescriptionReadability.computeFleschKincaidGradeLevel(testCase.data),
                testCase.flesh_kincaid_grade_level
        );
    }

}