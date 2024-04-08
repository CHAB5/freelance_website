/**
 * @author Harsheen Kaur
 * Class for the feature Skills
 *
 */
package actors.services.skills;

import actors.api.ApiConnectorActorClassic;
import actors.services.employerprofile.EmployerInfoManagerClassic;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import static akka.pattern.Patterns.ask;

import models.ApiMetadata;
import models.Job;
import models.Project;
import util.Constants;
import util.ServiceCommand;
import util.Utility;
import wrapper.ProjectsListResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 *
 * This method returns a list of projects that have the same skill as the skill
 * on which the user clicked. First an api call is made with parameters as job_details = true
 * and query as the skill. Once the response is received, it is deserialized. Moving on, it
 * then searches for the given skill in the list of skills in the project list and displays
 * only those results with the same skill. Next, we limit the number of projects to 10 using
 * streams. Once completed, it sends the response back to the parent actor i.e.
 * ApiConnectorClassic
 *
 *
 * @return - Returns a list of projets which have the same skill as the input skill
 * @throws ExecutionException
 * @throws InterruptedException
 * @throws TimeoutException
 * @throws UnsupportedEncodingException
 */

public class SkillsActor extends AbstractActor {

    private final ActorRef ws;
    private String id;

    /**
     *
     * Constructor for SkillsActor
     * @param ws - Actor Ref to parent class
     * @param id - String for id
     *
     */
    public SkillsActor(ActorRef ws, String id) {
        this.ws = ws;
        this.id = id;
    }

    /**
     *
     * Method to create an actor
     * @param ws - Actor Ref to parent class
     * @param id - String for id
     * @return - returns the actor created
     *
     */
    public static Props getProps(ActorRef ws, String id) {
        return Props.create(SkillsActor.class, () -> new SkillsActor(ws, id));
    }

    /**
     * It defines the messages that the actor can handle.
     * @return - returns AbstractActor
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SkillsRequest.class, this::onSkillsInfoRequestHandler)
                .build();
    }

    private void onSkillsInfoRequestHandler(SkillsRequest skills) throws UnsupportedEncodingException {
        this.process(sender(), skills.query);
    }

    /**
     * Handles the request from the search to view projects based on the skill clicked
     * on. It takes the query and reference to the parent actor. It calls the api,
     * performs functions on it and sends the result back to the parent actor.
     *
     * @param sender - reference to the parent actor
     * @param query - skill to be searched and displayed result for
     * @throws UnsupportedEncodingException
     *
     */
    private void process(ActorRef sender, String query) throws UnsupportedEncodingException {
        Map<String, Object> queryParamsMap = new HashMap<String, Object>(Constants.BASIC_QUERY_PARAM_MAP);
        List<Project> resp = new ArrayList<>();
        String encodedValue = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        queryParamsMap.put("query", encodedValue);
        queryParamsMap.put("job_details", true);
        ApiMetadata metadata = new ApiMetadata(
                Constants.BASE_URL_PROD + "/api/projects/0.1/projects/active/",
                "GET",
                new HashMap<String, String>(),
                "",
                queryParamsMap
        );
        ask(
                getContext().actorOf(ApiConnectorActorClassic.getProps(metadata)),
                new ApiConnectorActorClassic.CalloutSignal(
                        getSelf(), metadata
                ),
                Duration.ofMillis(15000)
        )
                .toCompletableFuture()
                .thenAccept(
                        (obj2) -> {
                            EmployerInfoManagerClassic.CalloutResponse responseWrapper2
                                    = (EmployerInfoManagerClassic.CalloutResponse) obj2;
                            responseWrapper2.futureHttpResponse
                                    .thenAccept(
                                            projectResp -> {
                                                ProjectsListResponse projResp = (ProjectsListResponse) Utility.deserialize(projectResp.body(), "ProjectsListResponse");
                                                List<Project> lstProjects = new ArrayList<>();
                                                for (Project project : projResp.result.projects) {
                                                    for (Job job : project.getJobs()) {
                                                        if (job.getName().equals(query)) {
                                                            lstProjects.add(project);
                                                        }
                                                    }
                                                }
                                                lstProjects = lstProjects
                                                        .stream()
                                                        .limit(10)
                                                        .collect(Collectors.toList());
                                                Skills info = new Skills(lstProjects, query);
                                                System.out.println(info);
                                                sender.tell(info, getSelf());
                                            }
                                    );
                        }
                );
    }

    public static class SkillsRequest implements ServiceCommand {
        final String query;

        public SkillsRequest(String query) {
            this.query = query;
        }
    }

    public static class Skills {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Skills that = (Skills) o;
            return (
                    this.getQuery() == that.getQuery()
//                    && this.getProjects() != null
//                    && that.getProjects() != null
//                    && this.getProjects().size() == that.getProjects().size()
            );
        }

        @Override
        public int hashCode() {
            return Objects.hash(projects, query);
        }
        static List<Project> projects;
        String query;

        public Skills(List<Project> projects, String query) {
            this.projects = projects;
            this.query = query;
        }

        public static List<Project> getProjects() {
            return projects;
        }

        public String getQuery() {
            return query;
        }
    }
}
