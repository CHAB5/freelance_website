package services.employerprofile;

import api.ApiConnector;
import util.Constants;
import models.Project;
import util.Utility;
import wrapper.ProjectsListResponse;
import wrapper.UserDetailsResponse;
import play.mvc.Result;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static play.mvc.Results.ok;

/**
 * Helper class for Employer Profile Page
 *
 * @author Aditya Joshi
 */
public class EmployerInfoHelper {
    /**
     * wrapper method for combining employer profile info result and projects list result
     *
     * @author Aditya Joshi
     * @param conn instance of ApiConnector class that makes callouts further
     * @param userId represents owner_id field in this case. Since employer is a kind of user the name is userId.
     * @return Asynchronous instance of Result object formed by combining employer profile info result and
     *         projects list result
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws IOException
     */
    public static CompletionStage<Result> getEmployerProfilePageDetails(ApiConnector conn, String userId) throws ExecutionException, InterruptedException, TimeoutException, IOException {
        CompletionStage<List<Project>> projectListResponse = EmployerInfoHelper.getProjectsList(conn, userId);
        CompletionStage<UserDetailsResponse> emplDetailResponse = EmployerInfoHelper.getEmployerDetails(conn, userId);
        return getResultData(projectListResponse, emplDetailResponse);
    }

    /**
     * method to combine async result of two callouts namely project list and employer profile details
     *
     * @author Aditya Joshi
     * @param projectListResponse async response object for project list API callout
     * @param emplDetailResponse async response object for employer details API callout
     * @return async Result that is combination of both the params
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws IOException
     */
    public static CompletionStage<Result> getResultData(CompletionStage<List<Project>> projectListResponse, CompletionStage<UserDetailsResponse> emplDetailResponse) throws ExecutionException, InterruptedException, TimeoutException, IOException {
        CompletionStage<Result> futureResult
                = emplDetailResponse.thenCombine(
                projectListResponse,
                (res1, res2) -> {
                    List<Project> lstProjects
                            = (List<Project>) res2
                            .stream()
                            .sorted(Comparator.comparingInt(Project::getSubmitdate).reversed())
                            .map((proj) -> {
                                proj.strDateValue = Utility.convertDate(proj.submitdate);
                                return proj;
                            })
                            .limit(Constants.NUM_OF_PROJECTS_ON_EMPLOYER_PROFILE)
                            .collect(Collectors.toList());
                    return ok(
                            views.html.userInfo.render((UserDetailsResponse) res1, lstProjects)
                    );
                }
        );
        return futureResult;
    }

    public static CompletionStage<List<Project>> getProjectsList(ApiConnector conn, String userId) throws ExecutionException, InterruptedException, TimeoutException {
        Map<String, Object> queryParamsMap = conn.getQueryParamsMap();
        queryParamsMap.put("full_description", false);
        queryParamsMap.put("sort_field", "submitdate");
        queryParamsMap.put("reverse_sort", false);
        queryParamsMap.put("owners[]", userId);
        conn.setQueryParamsMap(queryParamsMap);
        conn.setEndpointURL(Constants.BASE_URL_PROD + "/api/projects/0.1/projects");
        CompletionStage<String> projectsListFutureResponse
                = conn.sendRequest()
                .thenApply(HttpResponse::body);
        CompletionStage<List<Project>> asyncResult
                = projectsListFutureResponse.thenApply(
                (respStr) ->
                {
                    //System.out.println("============================ respStr ===> \n" + respStr);
//                    System.out.println((ProjectsListResponse) Utility.deserialize( respStr, "ProjectsListResponse"));
//                    System.out.println(((ProjectsListResponse) Utility.deserialize( respStr, "ProjectsListResponse")).result);
//                    System.out.println(((ProjectsListResponse) Utility.deserialize( respStr, "ProjectsListResponse")).result.projects);
//                    System.out.println(((ProjectsListResponse) Utility.deserialize( respStr, "ProjectsListResponse")).result.projects.size());
                    return (
                            (ProjectsListResponse) Utility.deserialize( respStr, "ProjectsListResponse")
                    ).result.projects;
                }
        );
        return asyncResult;
    }


    public static CompletionStage<UserDetailsResponse> getEmployerDetails(ApiConnector conn, String userId) throws ExecutionException, InterruptedException, TimeoutException {
        Map<String, Object> queryParamsMap = conn.getQueryParamsMap();
        queryParamsMap.putAll(Constants.QUERY_PARAM_EMPLOYER_INFO);
        conn.setQueryParamsMap(queryParamsMap);
        conn.setEndpointURL(Constants.BASE_URL_PROD + "/api/users/0.1/users/" + userId + "/");
        CompletionStage<String> projectsListFutureResponse
                = conn.sendRequest()
                .thenApply(HttpResponse::body);
        CompletionStage<UserDetailsResponse> asyncResult
                = projectsListFutureResponse.thenApply(
                (respStr) -> ((UserDetailsResponse) Utility.deserialize(respStr, "UserDetailsResponse"))
        );

        return asyncResult;
    }
}