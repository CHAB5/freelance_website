package actors.services.descriptionreadability;

import akka.actor.*;
import models.Project;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import akka.testkit.javadsl.TestKit;

import akka.actor.ActorSystem;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Test class for DescriptionReadability Actor service
 *
 * @author Shubham Punekar
 */
public class DescriptionReadabilityActorTest {

    static ActorSystem system;

    /**
     * Setting up Actor system fixture for all tests
     *
     * @author Shubham Punekar
     */
    @BeforeAll
    public static void setup() {
        system = ActorSystem.create();
    }

    /**
     * Shutting down Actor system fixture
     *
     * @author Shubham Punekar
     */
    @AfterAll
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * Test the processing of Indices message by DescriptionReadabilityActor to compute the readability indices
     * for the given project's preview description.
     *
     * @author Shubham Punekar
     */
    @Test
    public void testDescriptionReadabilityProcessIndices() {
        new TestKit(system) {
            {
                final Props descriptionReadabilityProps = DescriptionReadabilityActor.getProps();
                final ActorRef descriptionReadabilityActor = system.actorOf(descriptionReadabilityProps);
                final TestKit probe = new TestKit(system);

                // AverageIndices will be sent to this actor, this actor should then respond with an AverageIndicesResult
                DescriptionReadabilityProtocol.IndicesCommand indicesCommand = new
                        DescriptionReadabilityProtocol.IndicesCommand(
                        0,
                        "testProjectTitle",
                        "testProjectPreviewDescription");

                // Expected IndicesResult
                DescriptionReadabilityActor.IndicesResult expectedIndicesResult = new
                        DescriptionReadabilityActor.IndicesResult(
                        0,
                        "testProjectTitle",
                        "testProjectPreviewDescription"
                );
                descriptionReadabilityActor.tell(indicesCommand, probe.getRef());
                probe.expectMsg(Duration.ofSeconds(1), expectedIndicesResult);
            }
        };
    }

    /**
     * Test the processing of AverageIndices message by DescriptionReadabilityActor to compute average of readability indices
     * for the given project list's preview descriptions.
     *
     * @author Shubham Punekar
     */
    @Test
    public void testDescriptionReadabilityProcessAverageIndices() {
        new TestKit(system) {
            {
                final Props descriptionReadabilityProps = DescriptionReadabilityActor.getProps();
                final ActorRef descriptionReadabilityActor = system.actorOf(descriptionReadabilityProps);
                final TestKit probe = new TestKit(system);

                // AverageIndices will be sent to this actor, this actor should then respond with an AverageIndicesResult
                List<Project> testListOfProjects = new ArrayList<>();
                Project p1 = new Project();
                Project p2 = new Project();
                Project p3 = new Project();
                Project p4 = new Project();
                p1.preview_description = "The Australian platypus is seemingly a hybrid of a mammal and reptilian creature.";   // FREI: 45.64500000000001, FKGL: 8.383333802223206
                p2.preview_description = "This sentence, taken as a reading passage unto itself, is being used to prove a point.";  // FREI: 68.98250000000002, FKGL: 7.612500000000001
                p3.preview_description = "The cat sat on the mat.";  // FREI: 116.14500000000001, FKGL: -1.4499999999999993
                p4.preview_description = "#$can't9* \"ain't,\" 234ABC 23abn45 @#$aba34dfs#$% @a@e@i@o@u@"; // FREI: 45.64500000000001, 8.383333802223206
                testListOfProjects.add(p1);
                testListOfProjects.add(p2);
                testListOfProjects.add(p3);
                testListOfProjects.add(p4);

                CompletionStage<List<Project>> asyncListOfProjects = CompletableFuture.supplyAsync(() -> testListOfProjects);
                DescriptionReadabilityProtocol.AverageIndicesCommand averageIndicesCommand = new
                        DescriptionReadabilityProtocol.AverageIndicesCommand(asyncListOfProjects, "testSearchString");

                // Expected AverageIndicesResult
                DescriptionReadabilityActor.AverageIndicesResult expectedAverageIndicesResult = new
                        DescriptionReadabilityActor.AverageIndicesResult(
                        "testSearchString",
                        67.05697115384618,
                        6.452612323760987,
                        testListOfProjects
                );
                descriptionReadabilityActor.tell(averageIndicesCommand, probe.getRef());
                probe.expectMsg(Duration.ofSeconds(1), expectedAverageIndicesResult);
                Assertions.assertEquals(expectedAverageIndicesResult.toString(),
                        "AverageIndicesResult{" +
                                "searchString='" + "testSearchString" + '\'' +
                                ", averageFREI=" + 67.05697115384618 +
                                ", averageFKGL=" + 6.452612323760987 +
                                '}');
            }
        };
    }
}
















