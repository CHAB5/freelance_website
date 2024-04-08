package actors.api;
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
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Test class for ApiConnectorActorClassic
 *
 * @author Aditya Joshi
 */
class ApiConnectorActorClassicTest {

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
     * test method that invokes actor to make API callout
     *
     * @author Aditya Joshi
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testApiConnectorActorClassic() throws UnsupportedEncodingException {
        List<Project> resp = new ArrayList<>();
        String encodedValue = URLEncoder.encode("Java", StandardCharsets.UTF_8.toString());
        Map<String, Object> queryParamsMap2 = new HashMap<String, Object>(Constants.BASIC_QUERY_PARAM_MAP);
        queryParamsMap2.put("full_description", false);
        queryParamsMap2.put("sort_field", "submitdate");
        queryParamsMap2.put("reverse_sort", false);
        queryParamsMap2.put("owners[]", "123456");
        ApiMetadata metadata2 = new ApiMetadata(
                Constants.BASE_URL_PROD + "/api/projects/0.1/projects",
                "GET",
                new HashMap<String, String>(),
                "",
                queryParamsMap2
        );
        systemMock = ActorSystem.create();
        new TestKit(systemMock) {
            {
                final TestKit probe = new TestKit(systemMock);
                final ActorRef tar = systemMock.actorOf(ApiConnectorActorClassic.getProps(metadata2));
                tar.tell(new ApiConnectorActorClassic.CalloutSignal(probe.getRef(), metadata2), probe.getRef());
                CompletionStage<HttpResponse<String>> futureHttpResponse = new CompletableFuture<>();
                probe.expectMsg(new EmployerInfoManagerClassic.CalloutResponse(
                        futureHttpResponse,
                        null
                ));
            }
        };
    }
}