package controllers;

import actors.services.descriptionreadability.DescriptionReadabilityActor;
import actors.services.descriptionreadability.DescriptionReadabilityProtocol;
import actors.services.skills.SkillsActor;
import actors.services.wordstats.WordStatsActor;
import actors.services.employerprofile.EmployerInfoManagerClassic;
import actors.services.wordstats.WordStatsProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import api.ApiConnector;
import models.Project;
import play.cache.AsyncCacheApi;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;
import services.descriptionreadability.DescriptionReadability;
import scala.compat.java8.FutureConverters;
import services.employerprofile.EmployerInfoHelper;
import services.search.SearchHelper;
import services.search.SearchQuery;
import services.skills.Skills;
import util.Constants;
import util.Utility;
import services.wordstats.WordStats;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import static akka.pattern.Patterns.ask;

import akka.actor.*;
import views.html.averageIndices;
import views.html.indices;

import static akka.pattern.Patterns.ask;

/**
 * @author Aditya Joshi, Chandana Basavaraj, Harsheen Kaur, Shubham Punekar
 */

@Singleton
public class HomeController extends Controller {

    ActorSystem system;
    ActorRef wordStatsActor;
    ActorRef skillsActor;
    ActorRef descriptionReadabilityActor;

    private FormFactory formFactory;
    private MessagesApi messagesApi;
    private AsyncCacheApi asyncCacheApi;
    public static ApiConnector connector;

    ActorRef employerProfileRetrieverActorRef;

    static {
        connector = Utility.getApiConnector(
                Constants.BASE_URL_PROD,
                "GET",
                null,
                "",
                Constants.BASIC_QUERY_PARAM_MAP
        );
    }

    public HomeController() {
    }

    /**
     * Constructor for the HomeController Class
     * @param formFactory Factory for forms to be rendered in view
     * @param messagesApi messages api
     * @param asyncCacheApi caffeine cache
     * @param system Actor System
     */
    @Inject
    public HomeController(FormFactory formFactory, MessagesApi messagesApi, AsyncCacheApi asyncCacheApi, ActorSystem system) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
        this.asyncCacheApi = asyncCacheApi;

        this.system = system;
        this.wordStatsActor = system.actorOf(WordStatsActor.getProps());
        this.descriptionReadabilityActor = system.actorOf(DescriptionReadabilityActor.getProps());
        this.employerProfileRetrieverActorRef = system.actorOf(EmployerInfoManagerClassic.getProps(null, "test"));
        this.skillsActor = system.actorOf(SkillsActor.getProps(null, "test"));
    }

    private static HashMap<String, ArrayList<SearchQuery>> sessionSearchQueries;

    static {
        sessionSearchQueries = new HashMap<>();
    }

    /**
     * Displays the main search page of the project where user can enter query to be searched
     *
     * @param request - request from client used for determining session
     * @return - returns a form for user to enter search query
     */
    public CompletionStage<Result> index(Http.Request request) {
        Form<SearchQuery> searchQueryForm = formFactory.form(SearchQuery.class);
        return CompletableFuture.supplyAsync(
                () -> {
                    String userAgent = request.getHeaders().get("User-Agent").get();
                    return ok(views.html.index.render(searchQueryForm, messagesApi.preferred(request)))
                            .addingToSession(request, "session", userAgent);
                });
    }

    /**
     * @param request - request from client used for determining session
     * @return - redirects to the search view
     * @throws UnsupportedEncodingException
     * @author Shubham Punkear
     * Creates a queue of the searched queries per session according to limit search queries
     * @see Constants#LIMIT_SEARCH_QUERIES
     */
    public CompletionStage<Result> queryFreeLancer(Http.Request request) throws UnsupportedEncodingException {
        // Create form and bind it to a searchQuery object
        Form<SearchQuery> searchQueryForm = formFactory.form(SearchQuery.class);
        SearchQuery searchQuery = searchQueryForm.bindFromRequest(request).get();

        // Get the queue of search queries for the session from the hashmap if already created, else create one
        String session = Utility.getSession(request);

        List<SearchQuery> searchQueries = sessionSearchQueries.computeIfAbsent(session, s -> new ArrayList<SearchQuery>());

        // Maintain a list of search queries in queue
        if (searchQueries.size() == Constants.LIMIT_SEARCH_QUERIES) {
            searchQueries.remove(0);
        }
        searchQueries.add(searchQuery);

//        System.out.println("PRINTING Async MAP");
//        asyncSearchQueriesProjectLists.thenApply(map -> map.values().toString()).thenAccept(System.out::println);
//        return CompletableFuture.supplyAsync(() -> redirect(routes.HomeController.search()));
        return CompletableFuture.supplyAsync(() -> redirect(routes.HomeController.search()));
    }

    /**
     * @param request - request from client used for determining session
     * @return - renders the view to search
     * @throws UnsupportedEncodingException
     * @author Shubham Punkear
     * Takes in the query entered by the user, finds the list of project related to the query
     * and returns them to the search view
     */
    public CompletionStage<Result> search(Http.Request request) throws UnsupportedEncodingException {
        Form<SearchQuery> searchQueryForm = formFactory.form(SearchQuery.class);
        // Create a CF which returns a HashMap of search queries and their list of projects
        CompletionStage<LinkedHashMap<SearchQuery, List<Project>>> asyncSearchQueriesProjectLists =
                CompletableFuture.supplyAsync(LinkedHashMap::new);

        String session = Utility.getSession(request);
        List<SearchQuery> searchQueries = sessionSearchQueries.get(session);
        if (searchQueries == null) {
            return CompletableFuture.supplyAsync(() -> redirect(routes.HomeController.index()));
        }
        // Note that searchQueries is masquerading as a queue, so the order of this list shouldn't be modified
        // Therefore we create a current copy of current list (i.e. queue) and reverse it for displaying new results on top
        List<SearchQuery> reversedSearchQueries = new ArrayList<>(searchQueries);
        Collections.reverse(reversedSearchQueries);
        for (SearchQuery s : reversedSearchQueries) {
            asyncSearchQueriesProjectLists = asyncSearchQueriesProjectLists.thenCombineAsync(
//                    SearchHelper.getAsyncProjectsListResponse(s),
                    asyncCacheApi.getOrElseUpdate(
                            s.getSearchString(),
                            () -> SearchHelper.getAsyncProjectsListResponse(connector, s)
                    ),
                    (map, lop) -> {
                        lop = lop.stream().limit(Constants.LIMIT_PROJECTS_PER_QUERY).collect(Collectors.toList());
                        map.put(s, lop);
                        return map;
                    }
            );
        }

        // Pass on the asyncSearchQueriesProjectLists hashmap to the index page to be rendered
        return asyncSearchQueriesProjectLists.thenApply(
                (map) -> {
                    return ok(views.html.search.render(
                            searchQueryForm,
                            map,
                            messagesApi.preferred(request)));
                }
        );
    }

    /**
     * @param userId - User ID of the project
     * @return - returns the employer information
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     * @author Aditya Joshi
     * Displays the user information based on the given user id.
     * Redirects to the userInfo view when clicked on owner id in the project list displayed.
     */
    public CompletionStage<Result> userInfo(String userId) throws IOException, ExecutionException, InterruptedException, TimeoutException {
//        return EmployerInfoHelper.getEmployerProfilePageDetails(connector, userId);
//        CompletionStage<Result> result =
        System.out.println("I am hereeee!! ");
        //----
//        return FutureConverters.toJava(
//                ask(
//                        this.employerProfileRetrieverActorRef,
//                        new EmployerInfoManagerClassic.EmployerInfoRequest(
//                                "25832131",
//                                null,
//                                "testRequestId"
//                        ),
//                        10000
//                )
//        )
//        .thenApply(
//                response -> {
//                    List<Project> lstP = new ArrayList<Project>();
//                    return ok(views.html.userInfo.render((UserDetailsResponse) response, lstP));
////
////                    System.out.println("*-*-*-*-*-*-*-*- " + response);
////                    System.out.println(response.getClass().getName());
////
////                    CompletionStage<HttpResponse<String>> futureResp =
////                            (CompletionStage<HttpResponse<String>>) response;
////                    return futureResp.thenApply(
////                            (interiorResp) -> {
////                                System.out.println("interiorResp = " + interiorResp);
////                                UserDetailsResponse userDetailsResponse = (UserDetailsResponse) Utility.deserialize(interiorResp.body(), "UserDetailsResponse");
////
////                            }
////                    );
//                }
//        );
        //----
//        List<Project> lstP = new ArrayList<Project>();
//        return (CompletionStage<Result>) CompletableFuture.supplyAsync(() -> ok(views.html.userInfo.render(new UserDetailsResponse(), lstP)));
        return FutureConverters.toJava(
                ask(
                        this.employerProfileRetrieverActorRef,
                        new EmployerInfoManagerClassic.EmployerInfoRequest(
                                userId,
                                null,
                                "testRequestId"
                        ),
                        10000
                )
        )
        .thenApply(
                (infoObj) -> {
                    System.out.println(infoObj);
                    EmployerInfoManagerClassic.EmployerInfo info = (EmployerInfoManagerClassic.EmployerInfo) infoObj;
                    return ok(views.html.userInfo.render(info.getEmployerDetails(), info.getProjects()));
                }
        );
    }


    /**
     * @param name - Name of the skill for which the projects have to be displayed
     * @return - Returns a new html page displaying the projects for the searched skill
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws IOException
     * @author Harsheen Kaur
     * Called when the user clicks on any given
     * skill of a project displayed on the main page. It then returns a new page
     * with a list of projects with the particular skill.
     */
    public CompletionStage<Result> getSkills(String name) throws ExecutionException, InterruptedException, TimeoutException, IOException {
//        CompletionStage<List<Project>> projectListResponse = Skills.getProjectsList(connector, name);
////        projectListResponse.thenApply(HomeController::limitProjects);
//        return projectListResponse.thenApplyAsync(res -> ok(views.html.skills.render(res, name)));
        return FutureConverters.toJava(
                        ask(
                                this.skillsActor,
                                new SkillsActor.SkillsRequest(
                                        name
                                ),
                                10000
                        )
                )
                .thenApply(
                        (infoObj) -> {
                            System.out.println(infoObj);
                            SkillsActor.Skills info = (SkillsActor.Skills) infoObj;
                            return ok(views.html.skills.render(info.getProjects(), name));
                        }
                );
    }

    /**
     * @param id - OWNER ID of the project displayed.
     * @return - Redirects the page to the project description page for the
     * clicked title on the freelancer website.
     * @author Harsheen Kaur
     * Redirects the user to the freelancer website for
     * the specific project when clicked on the project title.
     */
    public Result freelancer(int id) {
        String url = "https://www.freelancer.com/projects/" + id + "/details";
        return redirect(url);
        //return ok(views.html.freelancer.render());
    }

//    public CompletionStage<Result> wordStats(String searchString) throws IOException, ExecutionException, InterruptedException, TimeoutException {
//        return WordStats.getWordStatsPageDetails(connector, searchString);
//    }


//    public CompletionStage<Result> wordStatsIndividual(String description) throws IOException, ExecutionException, InterruptedException, TimeoutException {
//        return WordStats.wordFrequencyIndividual(description);
//    }



    /**
     * Passes a message to DescriptionReadabilityActor to compute Flesch Reading Ease Index and Flesch Kincaid Grade Level
     * for all projects for a given query and render it to the HTML view
     * @param searchString - query
     * @return - average indices displayed in the average indices view
     * @author Shubham Punkear
     *
     */
    public CompletionStage<Result> averageIndices(String searchString) {
        CompletionStage<List<Project>> asyncListOfProjects = asyncCacheApi.getOrElseUpdate(
                searchString,   // searchString
                () -> SearchHelper.getAsyncProjectsListResponse(connector, new SearchQuery(searchString))   // searchString
        );
        return FutureConverters
                .toJava(
                        ask(
                                descriptionReadabilityActor,
                                new DescriptionReadabilityProtocol.AverageIndicesCommand(asyncListOfProjects, searchString),
                                10000)
                ).thenApply(averageIndicesResult -> {
                    return ok(averageIndices.render(
                            ((DescriptionReadabilityActor.AverageIndicesResult) averageIndicesResult).getSearchString(),
                            ((DescriptionReadabilityActor.AverageIndicesResult) averageIndicesResult).getAverageFREI(),
                            ((DescriptionReadabilityActor.AverageIndicesResult) averageIndicesResult).getAverageFKGL(),
                            ((DescriptionReadabilityActor.AverageIndicesResult) averageIndicesResult).getListOfProjects()
                    ));
                });
    }

    /**
     * Passes a message to DescriptionReadabilityActor to compute Flesch Reading Ease Index and Flesch Kincaid Grade Level
     * for individual projects for a given query and render it to the HTML view     *
     * @param projectID id of the project, as fetched from freelancer
     * @param projectTitle title of the project, as fetched from freelancer
     * @param projectPreviewDescription preview description of the project, as fetched from freelancer
     * @return average indices displayed in the indices view
     * @author Shubham Punekar
     */
    public CompletionStage<Result> indices(Integer projectID, String projectTitle, String projectPreviewDescription) {
        return FutureConverters.toJava(
                ask(
                        descriptionReadabilityActor,
                        new DescriptionReadabilityProtocol.IndicesCommand(projectID, projectTitle, projectPreviewDescription),
                        10000)
        ).thenApply(
                indicesResult -> {
                    return ok(indices.render(
                            ((DescriptionReadabilityActor.IndicesResult) indicesResult).getProjectID(),
                            ((DescriptionReadabilityActor.IndicesResult) indicesResult).getProjectTitle(),
                            ((DescriptionReadabilityActor.IndicesResult) indicesResult).getProjectPreviewDescription()
                    ));
                }
        );
    }

    /**
     * Passes a message to WordStatsActor to compute Frequency of unique words in the Project Description
     * for the list of projects for a given search string query and render it on the HTML page view
     *
     * @param searchString
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public CompletionStage<Result> wordStats(String searchString) throws IOException, ExecutionException, InterruptedException, TimeoutException
    {
        CompletionStage<List<Project>> asyncListOfProjects = asyncCacheApi.getOrElseUpdate(
                searchString,   // searchString
                () -> SearchHelper.getAsyncProjectsListResponse(connector, new SearchQuery(searchString))   // searchString
        );
        return FutureConverters
                .toJava(
                        ask(
                                wordStatsActor,
                                new WordStatsProtocol.GlobalStatsCommand(asyncListOfProjects),
                                10000)
                ).thenApply(
                        (frequencyMap) -> {
                            return ok(
                                    views.html.wordstats.render((Map<String, Integer>) frequencyMap)
                            );
                        });
    }

    /**
     * Passes a message to WordStatsActor to compute Frequency of unique words in the Project Description
     * for individual projects for a given search string query and render it on the HTML page view
     *
     * @param previewDescription
     * @return
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public CompletionStage<Result> wordStatsIndividual(String previewDescription) throws IOException, ExecutionException, InterruptedException, TimeoutException {

        return FutureConverters
                .toJava(
                        ask(
                                wordStatsActor,
                                new WordStatsProtocol.IndividualStatsCommand(previewDescription),
                                10000)
                ).thenApply(
                        (frequencyMap) -> {
                            return ok(
                                    views.html.wordstats.render((Map<String, Integer>) frequencyMap)
                            );
                        });
    }
}