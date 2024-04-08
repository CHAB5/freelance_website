package services.wordstats;

import api.ApiConnector;
import com.github.benmanes.caffeine.cache.AsyncCache;
import controllers.HomeController;
import models.Project;
import play.cache.AsyncCacheApi;
import services.search.SearchHelper;
import services.search.SearchQuery;
import util.Utility;
import wrapper.ProjectsListResponse;
import util.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.*;

import play.mvc.Result;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.stream.Collectors.toMap;
import static play.mvc.Results.ok;

/**
 * Helper for Word Stats calculation
 *
 * @author Chandana Basavaraj
 */
public class WordStats {

    /**
     *
     * This method returns a Result that has HashMap of unique words of the all the latest
     * projects that the user has searched with a word or phrase in the search textbox on index page.
     * An api call is made to fetch all the projects that match with the search word or phrase
     * Once the response is received, it is passed on to wordFrequency method. The Map of unique words
     * returned by the wordFrequency method is then passed on to make a call to the html page for rendering,
     * which is done using thenApply and CompletionStage<Result>
     *
     * @author Chandana Basavaraj
     * @return Returns a Result which has the Map of unique words as keys and their number of occurrences as value
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws IOException
     */
    public static CompletionStage<Result> getWordStatsPageDetails(ApiConnector conn, String searchString) throws ExecutionException, InterruptedException, TimeoutException, IOException {
        CompletionStage<List<Project>> projectListResponse = WordStats.getGlobalProjectsList(conn, searchString);

        return projectListResponse
                .thenApply(
                    WordStats::wordFrequency
                ).thenApply(
                        (frequencyMap) -> {
                            return ok(
                                    views.html.wordstats.render(frequencyMap)
                            );
                        }
                );
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
//        System.out.println("lst = " + lstProjects);
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

//        System.out.println("List of unique words and their count: ");
//        for(String key: wordsfrequencyMap.keySet())
//        {
//            System.out.println(key+ " : " + wordsfrequencyMap.get(key));
//        }


        sortedWordsfrequencyMap = new LinkedHashMap<String, Integer>();
        sortedWordsfrequencyMap = wordsfrequencyMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

//        System.out.println("Sorted(Most frequent to least frequent) List of unique words and their count: ");
        for(String key: sortedWordsfrequencyMap.keySet())
        {
//            System.out.println(key+ " : " + sortedWordsfrequencyMap.get(key));
        }
        } else {

        }
        return sortedWordsfrequencyMap;
    }

    /**
     *
     * This method returns a Result OK, rendering the Map of unique words as keys and values, of preview_description
     * of the individual project in wordstats html page, when the user clicks on a certain wordstats hyperlink listed
     * against each of the project on the main page. The call to getWordFrequencyMap Method is made Asynchronously using
     * CompletableFuture which returns CompletionStage<Result>
     *
     * @author Chandana Basavaraj
     * @param description Preview Description of Individual Projects
     * @return Returns a Result OK, which renders the Map of unique words as keys and values in wordstats page
     */

    public static CompletionStage<Result> wordFrequencyIndividual(String description)
    {
        CompletionStage<Result> futureResultMap
                = CompletableFuture.supplyAsync(
                    () -> getWordFrequencyMap(description)
                ).thenApply(
                    (mapOfWordFrequencies) -> ok(
                        views.html.wordstats.render(
                                mapOfWordFrequencies
                        )
                )
        );
        return futureResultMap;
    }

    /**
     * This method returns a Map of unique words of the preview_description field parameter it receives.
     * First, a Map of unique words and their corresponding count of occurrences, is created.
     * Further, using Streams, a Map of unique words is sorted, from most
     * frequently occuring to least, in the given preview_description. This sorted map is returned from the method.
     *
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
//            System.out.println(key+ " : " + frequencyMap.get(key));
        }

        Map<String, Integer> sortedfrequencyMap = new LinkedHashMap<String, Integer>();

        sortedfrequencyMap = frequencyMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        for(String key: sortedfrequencyMap.keySet())
        {
//            System.out.println(key+ " : " + sortedfrequencyMap.get(key));
        }

        return sortedfrequencyMap;
    }

    /**
     * This method returns a List of all the latest active projects that the user has searched with
     * a word or phrase in the search textbox on index page.An api call is made to fetch all the
     * projects that match with the search word or phrase, the response received is then deserialized,
     * these are done Asynchronously using CompletableFuture which returns CompletionStage<Result>
     *
     * @author Chandana Basavaraj
     * @return Returns a List of Projects from the api response
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public static CompletionStage<List<Project>> getGlobalProjectsList(ApiConnector conn, String searchString) throws ExecutionException, InterruptedException, TimeoutException, UnsupportedEncodingException {
//        CompletionStage<List<Project>> asyncResult = asyncCache.getOrElseUpdate(
//                searchString,
//                () -> SearchHelper.getAsyncProjectsListResponse(new SearchQuery(searchString))
//        );

//        Map<String, Object> queryParamsMap = new HashMap<String, Object>(Constants.BASIC_QUERY_PARAM_MAP);
//        System.out.println("BASIC_QUERY_PARAM_MAP = ");
//        for(String i:queryParamsMap.keySet()) {
//            System.out.println(i + " : "+ queryParamsMap.get(i));
//        }
        Map<String, Object> queryParamsMap = conn.getQueryParamsMap();
        queryParamsMap.put("query", URLEncoder.encode(searchString, StandardCharsets.UTF_8.toString()));
        queryParamsMap.put("job_details", true);
        conn.setQueryParamsMap(queryParamsMap);
        conn.setEndpointURL("https://www.freelancer.com/api/projects/0.1/projects/active");
        CompletionStage<String> projectsListFutureResponse = conn
                .sendRequest()
                .thenApply(HttpResponse::body);



        CompletionStage<List<Project>> asyncResult
                = projectsListFutureResponse.thenApply(
                (response) ->
                {
//                    System.out.println("response \n" + response);
                    return ((ProjectsListResponse) Utility.deserialize(response, "ProjectsListResponse")).result.projects;
                }
        );

        return asyncResult;
    }
}
