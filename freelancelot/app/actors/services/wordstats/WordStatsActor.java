package actors.services.wordstats;
import actors.services.descriptionreadability.DescriptionReadabilityActor;
import akka.actor.AbstractActor;
import akka.actor.Props;
import models.Project;
import services.wordstats.WordStats;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.stream.Collectors.toMap;

/**
 * Helper for Word Stats calculation using Akka
 *
 * @author Chandana Basavaraj
 */
public class WordStatsActor extends AbstractActor {

    /**
     * get properties for creating an Actor
     *
     * @author Chandana Basavaraj
     * @return Props
     */
    public static Props getProps() {
        return Props.create(WordStatsActor.class);
    }

    /**
     * Static container class for WordStats Actor
     * with attributes for a list of projects.
     *
     * @author Chandana Basavaraj
     */
    public static class StatsResult {
        private String projectPreviewDescription;

        /**
         * parameterized constructor for StatsResult
         *
         * @author Chandana Basavaraj
         * @param projectPreviewDescription
         */
        public StatsResult(String projectPreviewDescription) {
            this.projectPreviewDescription = projectPreviewDescription;
        }

        /**
         * gets the projectPreviewDescription
         *
         * @author Chandana Basavaraj
         * @return
         */
        public String getProjectPreviewDescription() {
            return projectPreviewDescription;
        }
    }

    /**
     * This method returns a Map of unique words of the preview_description field parameter it receives.
     * First, a Map of unique words and their corresponding count of occurrences, is created.
     * Further, using Streams, a Map of unique words is sorted, from most
     * frequently occuring to least, in the given preview_description. This sorted map is returned from the method.
     *
     * @author Chandana Basavaraj
     * @param description Preview Description of Individual Projects
     * @return Returns a Map of unique words as keys and their number of occurrences as value
     */
    public static Map<String, Integer> getWordFrequencyMap(String description)
    {
        String[] words = description.replaceAll("^[.,\\s]+", "").split("[.,\\s]+");

        Map<String, Integer> frequencyMap = new HashMap<String, Integer>();
        for (String word : words) {
            Integer count = frequencyMap.get(word);
            count = (count == null) ? 1 : ++count;
            frequencyMap.put(word, count);
        }

        for(String key: frequencyMap.keySet())
        {
            System.out.println(key+ " : " + frequencyMap.get(key));
        }

        Map<String, Integer> sortedfrequencyMap = new LinkedHashMap<String, Integer>();

        sortedfrequencyMap = frequencyMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        for(String key: sortedfrequencyMap.keySet())
        {
            System.out.println(key+ " : " + sortedfrequencyMap.get(key));
        }

        return sortedfrequencyMap;
    }


    /**
     *
     * This method returns a Map of unique words of the preview_description field of the list of
     * projects that is passed to it. First, a Map of unique words and their corresponding count of
     * occurrences, is created. Further, using Streams, a Map of unique words is sorted, from most
     * frequently occuring to least, in the given list of projects. This sorted map is returned from the method.
     *
     * @author Chandana Basavaraj
     * @param lstProjects - List of Projects
     * @return - Returns a Map of unique words as keys and their number of occurrences as value
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws IOException
     */
    public static Map<String, Integer> wordFrequency(List<Project> lstProjects)
    {

        Map<String, Integer> wordsfrequencyMap = null;
        Map<String, Integer> sortedWordsfrequencyMap = null;
        System.out.println("lst = " + lstProjects);
        if(lstProjects != null && !lstProjects.isEmpty()) {
            String preview_description = "";

            for(Project i : lstProjects) {
                preview_description += i.preview_description;
            }

            String[] allWords = preview_description.replaceAll("^[.,\\s]+", "").split("[.,\\s]+");

            wordsfrequencyMap = new HashMap<String, Integer>();
            Integer wordCountIterator = 0;

            for (String word : allWords) {
                wordCountIterator = wordsfrequencyMap.get(word);

                if (wordCountIterator == null)
                    wordCountIterator = 1;
                else
                    wordCountIterator = ++wordCountIterator;

                wordsfrequencyMap.put(word, wordCountIterator);
            }

            System.out.println("List of unique words and their count: ");
            for(String key: wordsfrequencyMap.keySet())
            {
                System.out.println(key+ " : " + wordsfrequencyMap.get(key));
            }


            sortedWordsfrequencyMap = new LinkedHashMap<String, Integer>();
            sortedWordsfrequencyMap = wordsfrequencyMap
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

            System.out.println("Sorted(Most frequent to least frequent) List of unique words and their count: ");
            for(String key: sortedWordsfrequencyMap.keySet())
            {
                System.out.println(key+ " : " + sortedWordsfrequencyMap.get(key));
            }
        } else {

        }
        return sortedWordsfrequencyMap;
    }

    /**
     * This method defines which two messages the WordStats Actor can handle for individual and global stats,
     * along with the implementation of these messages received must be handled for the respective stats.
     * These behaviors are built with a builder named ReceiveBuilder.
     *
     * @author Chandana Basavaraj
     * @return Receive
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WordStatsProtocol.IndividualStatsCommand.class, command -> {
                    var sender = sender();
                    CompletableFuture.supplyAsync(
                            () -> getWordFrequencyMap(command.getProjectPreviewDescription())
                    ).thenAccept(
                            (frequencyMap) -> {
                                System.out.println(" *******ASYNCHRONOUS INDIVIDUAL********** : " + sender.path().toString());
                                sender.tell(frequencyMap, self());
                            }
                    );
                })
                .match(WordStatsProtocol.GlobalStatsCommand.class, command -> {
                    var sender = sender(); // effectively final copy of the variable
                    System.out.println(" *******ASYNCHRONOUS GLOBAL********** : ");
                    System.out.println(" *******BEFORE ASYNC********** : " + sender.path().toString());
                    command.getAsyncListOfProjects().thenApply(
                            WordStatsActor::wordFrequency
                    ).thenAccept(
                            (frequencyMap) -> {
                                sender.tell(frequencyMap, self());
                            }
                    );
                })
                .build();
    }
}
