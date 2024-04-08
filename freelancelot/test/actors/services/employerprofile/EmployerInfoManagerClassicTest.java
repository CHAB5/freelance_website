package actors.services.employerprofile;
import actors.api.ApiConnectorActorClassic;
import actors.services.employerprofile.EmployerInfoManagerClassic;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import models.ApiMetadata;
import models.Project;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import akka.testkit.javadsl.TestKit;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import util.Constants;
import wrapper.UserDetailsResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Test class for EmployerInfoManagerClassic
 * @author Aditya Joshi
 */
class EmployerInfoManagerClassicTest {

    @Mock
    static ActorSystem systemMock;

    /**
     * Setup method to initiate mocks in the Mockito framework
     * @author Aditya Joshi
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    ActorRef ws;

    /**
     * test method that invokes actor to get employer profile info and projects
     * @author Aditya Joshi
     */
    @Test
    public void testEmployerActor(){
        List<Project> projectList = new ArrayList<>();
        systemMock = ActorSystem.create();
        new TestKit(systemMock) {
            {
                final TestKit probe = new TestKit(systemMock);
                final ActorRef tar = systemMock.actorOf(EmployerInfoManagerClassic.getProps(ws, "test"));
                tar.tell(
                        new EmployerInfoManagerClassic.EmployerInfoRequest(
                        "61173746",
                        null,
                        "testRequestId"
                        ),
                        probe.getRef()
                );
                UserDetailsResponse resp = new UserDetailsResponse();
                resp.result.id = 61173746;
                EmployerInfoManagerClassic.EmployerInfo info
                        = new EmployerInfoManagerClassic.EmployerInfo(
                        resp,
                        new ArrayList<Project>()
                );
                info.hashCode();
                assertEquals(
                        0,
                        info.getProjects().size()
                );
                probe.expectMsg(
                        info
                );
            }
        };
    }
}