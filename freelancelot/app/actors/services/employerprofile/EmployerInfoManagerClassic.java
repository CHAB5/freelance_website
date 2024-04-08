package actors.services.employerprofile;

import actors.api.ApiConnectorActorClassic;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import static akka.pattern.Patterns.ask;

import models.ApiMetadata;
import models.Project;
import util.Constants;
import util.ServiceCommand;
import util.Utility;
import wrapper.ProjectsListResponse;
import wrapper.UserDetailsResponse;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Actor to get employer profile info and projects
 *
 * @author Aditya Joshi
 */
public class EmployerInfoManagerClassic extends AbstractActor {

    // ************* State START *************
    private final ActorRef ws;
    private ActorRef supervisorActor;
    private String requestId;
    private String id;
    // ************* State END *************

    /**
     * Parameterized Constructor
     *
     * @param ws ActorRef of parent actor
     * @param id id of this actor - for future compatibility
     */
    public EmployerInfoManagerClassic(ActorRef ws, String id) {
        this.ws = ws;
        this.id = id;
    }

    /**
     * Returns Props of this actor for creating this actor further
     *
     * @param ws ActorRef of parent actor
     * @param id id of this actor - for future compatibility
     * @return props of this actor class
     */
    public static Props getProps(ActorRef ws, String id) {
        return Props.create(EmployerInfoManagerClassic.class, () -> new EmployerInfoManagerClassic(ws, id));
    }

    /**
     * Method defines behavior of the actor when it receives a specific messages
     *
     * @author Aditya Joshi
     * @return Receive instance that is built by the messages it receives
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(EmployerInfoRequest.class, this::onEmplInfoRequestHandler)
                .build();
    }

    /**
     * Handler for <code>EmployerInfoRequest</code> type of message.
     *
     * @author Aditya Joshi
     * @param requestObj EmployerInfoRequest message
     */
    private void onEmplInfoRequestHandler(EmployerInfoRequest requestObj) {
        this.supervisorActor = requestObj.replyTo;
        this.requestId = requestObj.requestId;
        System.out.println("======= requestId : " + requestObj.requestId + "===============");
//        this.fetchEmployerProfileInfo1(sender(), requestObj.employerUserId);
        this.process(sender(), requestObj.employerUserId);
    }

    /**
     * Initiates api callouts for getting data about profile and projects by spawning api connector actor
     *
     * @author Aditya Joshi
     * @param sender ActorRef of the sender actor
     * @param employerId User Id of Employer
     */
    private void process(ActorRef sender, String employerId) {
        Map<String, Object> queryParamsMap1 = new HashMap<String, Object>(Constants.BASIC_QUERY_PARAM_MAP);
        queryParamsMap1.putAll(Constants.QUERY_PARAM_EMPLOYER_INFO);
        ApiMetadata metadata1 = new ApiMetadata(
                Constants.BASE_URL_PROD + "/api/users/0.1/users/" + employerId + "/",
                "GET",
                new HashMap<String, String>(),
                "",
                queryParamsMap1
        );
        Map<String, Object> queryParamsMap2 = new HashMap<String, Object>(Constants.BASIC_QUERY_PARAM_MAP);
        queryParamsMap2.put("full_description", false);
        queryParamsMap2.put("sort_field", "submitdate");
        queryParamsMap2.put("reverse_sort", false);
        queryParamsMap2.put("owners[]", employerId);
        ApiMetadata metadata2 = new ApiMetadata(
                Constants.BASE_URL_PROD + "/api/projects/0.1/projects",
                "GET",
                new HashMap<String, String>(),
                "",
                queryParamsMap2
        );


                ask(
                        getContext().actorOf(ApiConnectorActorClassic.getProps(metadata1)),
                        new ApiConnectorActorClassic.CalloutSignal(
                                getSelf(), metadata1
                        ),
                        Duration.ofMillis(15000)
                ).toCompletableFuture()
                        .thenAccept(
                                (obj1) -> {
                                    EmployerInfoManagerClassic.CalloutResponse responseWrapper1
                                            = (EmployerInfoManagerClassic.CalloutResponse) obj1;
                                    responseWrapper1.futureHttpResponse.thenAccept(
                                            (userResponse) -> {
                                                System.out.println(userResponse);
//                                                CompletableFuture<Object> res2 =
                                                    ask(
                                                            getContext().actorOf(ApiConnectorActorClassic.getProps(metadata2)),
                                                            new ApiConnectorActorClassic.CalloutSignal(
                                                                    getSelf(), metadata2
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
                                                                                    UserDetailsResponse userDetailsResponse = (UserDetailsResponse) Utility.deserialize(userResponse.body(), "UserDetailsResponse");
                                                                                    List<Project> lstProjects
                                                                                            = projResp.result.projects
                                                                                            .stream()
                                                                                            .sorted(Comparator.comparingInt(Project::getSubmitdate).reversed())
                                                                                            .map((proj) -> {
                                                                                                proj.strDateValue = Utility.convertDate(proj.submitdate);
                                                                                                return proj;
                                                                                            })
                                                                                            .limit(Constants.NUM_OF_PROJECTS_ON_EMPLOYER_PROFILE)
                                                                                            .collect(Collectors.toList());
                                                                                    EmployerInfo info = new EmployerInfo(userDetailsResponse, lstProjects);
                                                                                    sender.tell(info, getSelf());
                                                                                }
                                                                        );
                                                                    }
                                                            );
                                            }
                                    );
                                }
                        );

    }


    // ************* Message Prototype START *************

    /**
     * Message that this class retrieves
     */
    public static class EmployerInfoRequest implements ServiceCommand {
        final String employerUserId;
        final ActorRef replyTo;
        final String requestId;
        public EmployerInfoRequest(String employerUserId , ActorRef replyTo  , String requestId) {
            this.employerUserId = employerUserId;
            this.replyTo = replyTo;
            this.requestId = requestId;
        }
    }

    /**
     * Message that this class sends back to the parent class
     */
    public static class EmployerInfo {
        UserDetailsResponse employerDetails;
        List<Project> projects;

        public EmployerInfo(UserDetailsResponse employerDetails, List<Project> projects) {
            this.employerDetails = employerDetails;
            this.projects = projects;
        }

        public UserDetailsResponse getEmployerDetails() {
            return employerDetails;
        }

        public List<Project> getProjects() {
            return projects;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EmployerInfo that = (EmployerInfo) o;
            return (
                    this.getEmployerDetails().result.id == that.getEmployerDetails().result.id
//                    && this.getProjects() != null
//                    && that.getProjects() != null
//                    && this.getProjects().size() == that.getProjects().size()
            );
        }

        @Override
        public int hashCode() {
            return Objects.hash(employerDetails, projects);
        }
    }

    /**
     * Message that this actor receives from the API callout actor
     */
    public static class CalloutResponse implements ServiceCommand {
        public final CompletionStage<HttpResponse<String>> futureHttpResponse;
        public final HttpResponse<String> response;

        public CalloutResponse(
                CompletionStage<HttpResponse<String>> futureHttpResponse,
                HttpResponse<String> response
        ) {
            this.futureHttpResponse = futureHttpResponse;
            this.response = response;
        }

        @Override
        public boolean equals(Object obj) {
            return this.futureHttpResponse != null && ((CalloutResponse) obj).futureHttpResponse != null;
        }
    }
    // ************* Message Prototype END *************

}