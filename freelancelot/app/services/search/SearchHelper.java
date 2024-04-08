package services.search;

import api.ApiConnector;
import controllers.HomeController;
import controllers.routes;
import models.Project;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;
import util.Constants;
import util.Utility;
import wrapper.ProjectsListResponse;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * @author Shubham Punekar
 * Class to get the list of projects from api call to the production based on the search query
 */
public class SearchHelper {
    @Inject FormFactory formFactory;

    /**
     * @author Shubham Punekar
     * @param searchQuery - query
     * @param connector - Api connector
     * @return - returns an asynchronous list of projects
     * @throws UnsupportedEncodingException
     */
    public static CompletionStage<List<Project>> getAsyncProjectsListResponse(ApiConnector connector, SearchQuery searchQuery) throws UnsupportedEncodingException {
        // Fetch results from freelancer for the given search query
        Map<String, Object> queryParamsMap = connector.getQueryParamsMap();
        queryParamsMap.put("query", URLEncoder.encode(searchQuery.getSearchString(), StandardCharsets.UTF_8.toString()));
        queryParamsMap.put("job_details", true);
        connector.setQueryParamsMap(queryParamsMap);
        connector.setEndpointURL(Constants.BASE_URL_PROD + "/api/projects/0.1/projects/active");
        CompletionStage<String> projectsListFuture
                = connector.sendRequest()
                .thenApply(HttpResponse::body);

        CompletionStage<List<Project>> asyncProjectsListResponse
                = projectsListFuture.thenApply(
                (projectsListsResponseJSON) ->
                {
//                    System.out.println("###################################### " +projectsListsResponseJSON);
                    return ((ProjectsListResponse) Utility.deserialize(projectsListsResponseJSON, "ProjectsListResponse")).result.projects;
                }
        );

        return asyncProjectsListResponse;
    }
}
