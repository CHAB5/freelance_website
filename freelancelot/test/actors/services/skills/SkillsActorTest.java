/**
 * @author Harsheen Kaur
 *
 * Class for unit test for feature skills
 */
package actors.services.skills;
import actors.api.ApiConnectorActorClassic;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import models.ApiMetadata;
import models.Project;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import akka.testkit.javadsl.TestKit;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import util.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for Skills Actor class
 */
class SkillsActorTest {

    ActorRef ws;

    @Mock
    static ActorSystem systemMock;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    static ActorSystem system;

//    @AfterAll
//    public static void teardown() {
//        TestKit.shutdownActorSystem(system);
//        system = null;
//    }

    @Test
    public void testSkillActor(){

        List<Project> projectList = new ArrayList<>();
        systemMock = ActorSystem.create();
        new TestKit(systemMock) {
            {

                final Props skillsActorProps = SkillsActor.getProps(ws, "test");
                final ActorRef tar = systemMock.actorOf(skillsActorProps);
                final TestKit probe = new TestKit(systemMock);
                //final TestKit probe = new TestKit(system);

//                tar.tell(new SkillsActor.Skills(projectList, "Java"), probe.getRef());
                tar.tell(new SkillsActor.SkillsRequest("Java"), probe.getRef());
                SkillsActor.Skills obj = new SkillsActor.Skills(new ArrayList<>(), "Java");
                probe.expectMsg(obj);
            }
        };
    }

    @Test
    public void testSkills(){

        List<Project> projectList = new ArrayList<>();
        SkillsActor.Skills skills = new SkillsActor.Skills(projectList, "Java");
        var x = skills.getProjects();
        var y = skills.getQuery();
        Assertions.assertEquals(x, projectList);
        Assertions.assertEquals(y, "Java");
    }

    @Test
    public void testApiConnectorActorClassic() throws UnsupportedEncodingException {
        Map<String, Object> queryParamsMap = new HashMap<String, Object>(Constants.BASIC_QUERY_PARAM_MAP);
        List<Project> resp = new ArrayList<>();
        String encodedValue = URLEncoder.encode("Java", StandardCharsets.UTF_8.toString());
        queryParamsMap.put("query", encodedValue);
        queryParamsMap.put("job_details", true);
        ApiMetadata metadata = new ApiMetadata(
                Constants.BASE_URL_PROD + "/api/projects/0.1/projects/active/",
                "GET",
                new HashMap<String, String>(),
                "",
                queryParamsMap
        );
        systemMock = ActorSystem.create();
        new TestKit(systemMock) {
            {
//                final ActorRef tar1 = systemMock.actorOf(SkillsActor.getProps(ws, "test"));
                final ActorRef tar = systemMock.actorOf(ApiConnectorActorClassic.getProps(metadata));
                tar.tell(new ApiConnectorActorClassic.CalloutSignal(getRef(), metadata), getRef());
            }
        };
    }
}