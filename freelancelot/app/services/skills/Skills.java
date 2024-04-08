/**
 * @author Harsheen Kaur
 * Class for the feature Skills
 *
 */

package services.skills;

import api.ApiConnector;
import models.Job;
import models.Project;
import util.Constants;
import util.Utility;
import wrapper.ProjectsListResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class Skills {

    //TODO: Use a cached call for the same search query
    /**
     *
     * This method returns a list of projects that have the same skill as the skill
     * on which the user clicked. First an api call is made with parameters as job_details = true
     * and query as the skill. Once the response is received, it is deserialized. Moving on, it
     * then searches for the given skill in the list of skills in the project list and displays
     * only those results with the same skill. Next, we limit the number of projects to 10 using
     * streams.
     *
     * @param conn - ApiConnector object
     * @param name - Skill name for which results have to be displayed
     * @return - Returns a list of projets which have the same skill as the input skill
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws UnsupportedEncodingException
     */
    public static CompletionStage<List<Project>> getProjectsList(ApiConnector conn, String name) throws ExecutionException, InterruptedException, TimeoutException, UnsupportedEncodingException {
        Map<String, Object> queryParamsMap = conn.getQueryParamsMap();
        List<Project> resp = new ArrayList<>();
        String encodedValue = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        queryParamsMap.put("query", encodedValue);
        queryParamsMap.put("job_details", true);
        conn.setQueryParamsMap(queryParamsMap);
        conn.setEndpointURL(Constants.BASE_URL_PROD + "/api/projects/0.1/projects/active/");
        CompletionStage<String> projectsListFutureResponse
                = conn.sendRequest()
                .thenApply(HttpResponse::body);
        CompletionStage<List<Project>> asyncResult
                = projectsListFutureResponse.thenApply(
                (respStr) ->
                {
//                    System.out.println("============================ respStr ===> \n" + respStr);
//                    System.out.println((ProjectsListResponse) Utility.deserialize( respStr, "ProjectsListResponse"));
//                    System.out.println(((ProjectsListResponse) Utility.deserialize( respStr, "ProjectsListResponse")).result);
//                    System.out.println(((ProjectsListResponse) Utility.deserialize( respStr, "ProjectsListResponse")).result.projects);
//                    System.out.println(((ProjectsListResponse) Utility.deserialize( respStr, "ProjectsListResponse")).result.projects.size());
                    return (
                            (ProjectsListResponse) Utility.deserialize( respStr, "ProjectsListResponse")
                    ).result.projects;
                }
        );

//        asyncResult.thenApply(
//                projectList -> projectList.stream()
//                        .flatMap(project -> project.jobs.stream()
//                                .filter(job -> job.getName().equals(name)))
//                        .limit(10)
//                        .collect(Collectors.toList())
//
//        );

//        return asyncResult;

        asyncResult = asyncResult.thenApply(
                projectList -> {

                    for(Project project: projectList){
                        for(Job job: project.getJobs()){
                            if(job.getName().equals(name)){
                                resp.add(project);
                            }
                        }
                        //System.out.println("-----------------------" + resp);
                        //System.out.println(resp.size());
                    }
                   // System.out.println(resp.size());
                    return resp;
                }
        );
        asyncResult = asyncResult.thenApply(
                projectList -> projectList.stream()
                        .limit(10)
                        .collect(Collectors.toList())

        );
        return asyncResult;
    }
}

