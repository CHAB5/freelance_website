package actors.services.wordstats;

import akka.actor.*;
import models.Project;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import akka.testkit.javadsl.TestKit;

import akka.actor.ActorSystem;
import services.wordstats.WordStats;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Test class for WordStats Actor service
 *
 * @author Chandana Basavaraj
 */
public class WordStatsActorTest {

    static ActorSystem system;

    /**
     * Setting up Actor system fixture for all tests
     *
     * @author Chandana Basavaraj
     */
    @BeforeAll
    public static void setup() {
        system = ActorSystem.create();
    }

    /**
     * Shutting down Actor system fixture
     *
     * @author Chandana Basavaraj
     */
    @AfterAll
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    /**
     * Test case for wordFrequency method which takes individual project description as parameter
     * and probes the result returned from the WordStats Actor for the project
     *
     * @author Chandana Basavaraj
     */
    @Test
    public void testIndividualWordStats() {
        new TestKit(system) {
            {
                final Props individualStatsProps = WordStatsActor.getProps();
                final ActorRef wordStatsActor = system.actorOf(individualStatsProps);
                final TestKit probe = new TestKit(system);

                WordStatsProtocol.IndividualStatsCommand givenIndividualStatsCommand = new
                        WordStatsProtocol.IndividualStatsCommand(
                        "Apple is a fruit Montreal is a city");

                // Expected WordStatsMap
                WordStatsActor.StatsResult expectedindividualStats = new
                        WordStatsActor.StatsResult(
                        "testProjectPreviewDescription"
                );

                Map<String, Integer> testExpectedMap = new HashMap<String, Integer>();

                testExpectedMap.put("a", 2);
                testExpectedMap.put("is",2);
                testExpectedMap.put("Apple", 1);
                testExpectedMap.put("city",1);
                testExpectedMap.put("fruit", 1);
                testExpectedMap.put("Montreal", 1);

                wordStatsActor.tell(givenIndividualStatsCommand, probe.getRef());
                probe.expectMsg(Duration.ofSeconds(1), testExpectedMap);
            }
        };
    }

    /**
     * Test case for wordFrequency method which takes list of projects as parameter
     * and probes the result returned from the WordStats Actor for a list of projects
     *
     * @author Chandana Basavaraj
     */
    @Test
    public void testGlobalWordStats() {
        new TestKit(system) {
            {
                final Props individualStatsProps = WordStatsActor.getProps();
                final ActorRef wordStatsActor = system.actorOf(individualStatsProps);
                final TestKit probe = new TestKit(system);

                List<Project> projectsList = new ArrayList<>();
                Project proj1 = new Project();
                Project proj2 = new Project();
                proj1.preview_description = "As he travels further round the frequency increases still more.";
                proj2.preview_description = "Moreover, the frequency of sneezing should be the real concern.";

                CompletionStage<List<Project>> asyncListOfProjects = CompletableFuture.supplyAsync(() -> projectsList);
                WordStatsProtocol.GlobalStatsCommand globalStatsCommand = new
                        WordStatsProtocol.GlobalStatsCommand(asyncListOfProjects);

               WordStatsActor.StatsResult statsResult = new WordStatsActor.StatsResult(proj1.preview_description);
                proj1.preview_description = statsResult.getProjectPreviewDescription();

                projectsList.add(proj1);
                projectsList.add(proj2);

                Map<String, Integer> expectedGlobalStats = new HashMap<String, Integer>();

                expectedGlobalStats.put("the", 3);
                expectedGlobalStats.put("frequency",2);
                expectedGlobalStats.put("still", 1);
                expectedGlobalStats.put("be",1);
                expectedGlobalStats.put("more", 1);
                expectedGlobalStats.put("real", 1);
                expectedGlobalStats.put("concern", 1);
                expectedGlobalStats.put("travels", 1);
                expectedGlobalStats.put("increases", 1);
                expectedGlobalStats.put("As", 1);
                expectedGlobalStats.put("Moreover", 1);
                expectedGlobalStats.put("round", 1);
                expectedGlobalStats.put("sneezing", 1);
                expectedGlobalStats.put("of", 1);
                expectedGlobalStats.put("should", 1);
                expectedGlobalStats.put("further", 1);
                expectedGlobalStats.put("he", 1);

                wordStatsActor.tell(globalStatsCommand, probe.getRef());
                probe.expectMsg(Duration.ofSeconds(1), expectedGlobalStats);
            }
        };
    }
}
















