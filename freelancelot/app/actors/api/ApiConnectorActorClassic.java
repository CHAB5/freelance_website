package actors.api;

import actors.services.employerprofile.EmployerInfoManagerClassic;
import actors.services.skills.SkillsActor;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import models.ApiMetadata;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletionStage;

/**
 * ApiConnector class creates an HTTP connection that is used in the API callouts that are made inside makeCallout method.
 *
 * @author Aditya Joshi
 */
public class ApiConnectorActorClassic extends AbstractActor {

    // ************* Actor State START *************
    final private ApiMetadata apiMetadata;
    // ************* Actor State END *************

    /**
     * Parameterized Constructor
     *
     * @param apiMetadata API metadata required for API Callout
     */
    // ************* Constructor Definition START *************
    private ApiConnectorActorClassic(ApiMetadata apiMetadata) {
//        super(context);
        this.apiMetadata = apiMetadata;
    }
    // ************* Constructor Definition END *************

    /**
     * setup factory method for this actor
     *
     * @param metadata API metadata required for API Callout
     * @return props for this actor
     */
    // ************* Setup factory START *************
    public static Props getProps(ApiMetadata metadata) {
        return Props.create(ApiConnectorActorClassic.class, () -> new ApiConnectorActorClassic(metadata));
    }

    // ************* Setup factory END *************

    // ************* Message Prototype START *************

    /**
     * Generic Parent Interfact for all messages
     */
    public interface Command {}

    /**
     * Message class for this actor
     */
    public static class CalloutSignal implements Command {
        public final ApiMetadata metadata;
        public final ActorRef replyTo;
        public CalloutSignal(ActorRef replyTo, ApiMetadata metadata) {
            this.replyTo = replyTo;
            this.metadata = metadata;
        }
    }
    // ************* Message Prototype END *************

    /**
     * Method that listens to all messages that it receives
     *
     * @return Receive instance built by calloutsignal
     */
    // ************* Actor Behavior on Msg Receive START *************
    public Receive createReceive() {
        return receiveBuilder()
                .match(CalloutSignal.class, this::onInitCalloutSignalHandler)
                .build();
    }
    // ************* Actor Behavior on Msg Receive END *************

    // helper methods

    /**
     * Helper method that makes a callout on message reception
     *
     * @param signalInformation incoming message
     */
    private void onInitCalloutSignalHandler(CalloutSignal signalInformation) {
        System.out.println("!!!!!!!!!!! CALLING OUT API HERE !!!!!!!!!!!");
//        getContext().getLog().info("!!!!!!!!!!! CALLING OUT API HERE !!!!!!!!!!!");
        CompletionStage<HttpResponse<String>> futureHttpResponse = null;
//        System.out.println(cl.);
        futureHttpResponse = signalInformation.metadata
                .getHttpClient()
                .sendAsync(
                        signalInformation.metadata.getApiRequest(),
                        HttpResponse.BodyHandlers.ofString()
                )
//                .thenApply(resp -> {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    return resp;
//                })
        ;

        getSender().tell(
                new EmployerInfoManagerClassic.CalloutResponse(
                        futureHttpResponse,
                        null
                ),
                getSelf()
        );
    }

}



