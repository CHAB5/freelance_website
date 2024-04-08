package api;

import api.ApiConnector;
import models.Project;
import util.Constants;
import util.Utility;
import wrapper.ProjectsListResponse;
import wrapper.UserDetailsResponse;
import org.junit.jupiter.api.Test;
import play.mvc.Result;

import static org.mockito.Mockito.mock;
import static play.mvc.Http.Status.OK;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static play.mvc.Results.ok;

class ApiConnectorTest {


    /**
     @Test
     void testGetProjectsList() throws ExecutionException, InterruptedException, TimeoutException {
     ApiConnector connection = mock(ApiConnector.class);

     HttpResponse<String> mockResp = new HttpResponse<String>() {
     @Override
     public int statusCode() {
     return 200;
     }

     @Override
     public HttpRequest request() {
     return null;
     }

     @Override
     public Optional<HttpResponse<String>> previousResponse() {
     return Optional.empty();
     }

     @Override
     public HttpHeaders headers() {
     return null;
     }

     @Override
     public String body() {
     return "{'request_id':'123123123','result':{'projects':[]}}";
     }

     @Override
     public Optional<SSLSession> sslSession() {
     return Optional.empty();
     }

     @Override
     public URI uri() {
     return null;
     }

     @Override
     public HttpClient.Version version() {
     return null;
     }
     };

     CompletableFuture<HttpResponse<String>> futureResp = CompletableFuture.supplyAsync(() -> {return mockResp;});
     when(connection.sendRequest()).thenReturn(futureResp);
     System.out.println("***** here *****");
     CompletableFuture<List<Project>> resultInFuture =
     (CompletableFuture<List<Project>>) EmployerInfoHelper.getProjectsList(
     connection,
     "48465937"
     );
     assertNotNull(resultInFuture.get());
     verify(connection).sendRequest();
     }
     */

    /**
     * tests the constructors of ApiConnector
     */
    @Test
    void testConstructors() {
        ApiConnector conn1 = new ApiConnector();
        ApiConnector conn2 = new ApiConnector(
                "https://www.freelancer.com/api/users/0.1/users/" + "48465937" + "/",
                "GET",
                null,
                "",
                new HashMap<String, Object>(Constants.QUERY_PARAM_EMPLOYER_INFO)
        );
        ApiConnector conn3 = new ApiConnector(
                HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_1_1)
                        .connectTimeout(Duration.ofSeconds(5))
                        .build()
        );

        assertNotNull(conn1.getHttpClient());
        assertNotNull(conn2.getHttpClient());
        assertNotNull(conn3.getHttpClient());
    }

    /**
     * tests getters and setters of query param
     */
    @Test
    public void testGetterSetters() {
        ApiConnector c = new ApiConnector();
        c.setEndpointURL("test");
        assertEquals("test", c.getEndpointURL());
        c.setQueryParamsMap(new HashMap<String, Object>());
        assertEquals(0, c.getQueryParamsMap().size());
    }

    /**
     * tests getter of query param
     */
    @Test
    public void testGetQueryParamsMap() {
        Map<String, Object> mapObj = Utility.getApiConnector(
                Constants.BASE_URL_SANDBOX,
                "GET",
                null,
                "",
                Constants.BASIC_QUERY_PARAM_MAP
        ).getQueryParamsMap();
        assertEquals(
                Constants.BASIC_QUERY_PARAM_MAP.get("compact"),
                mapObj.get("compact")
        );
    }

    /**
     * tests setter of query param map
     */
    @Test
    public void testSetQueryParamsMap() {
        ApiConnector connector = new ApiConnector();
        Map<String, Object> queryParamsMap = new HashMap<String, Object> ();
        queryParamsMap.put("param1", "test");
        connector.setQueryParamsMap(
                queryParamsMap
        );
        assertEquals(
                "test",
                String.valueOf(connector.getQueryParamsMap().get("param1"))
        );
    }

    /**
     * tests endpoint getter
     */
    @Test
    public void testGetEndpointURL() {
        ApiConnector connector = Utility.getApiConnector(
                Constants.BASE_URL_SANDBOX ,
                "GET",
                null,
                "",
                null
        );
        assertEquals(
                Constants.BASE_URL_SANDBOX,
                connector.getEndpointURL()
        );
    }

    /**
     * tests setter of url endpoint
     */
    @Test
    public void testSetEndpointURL() {
        ApiConnector connector = new ApiConnector();
        connector.setEndpointURL(Constants.BASE_URL_SANDBOX);
        assertEquals(
                Constants.BASE_URL_SANDBOX,
                connector.getEndpointURL()
        );
    }

    /**
     * tests headers setter
     */
    @Test
    public void testSetHeaders() {
        Map<String, String> mapHeaders = new HashMap<String, String>();
        mapHeaders.put("key1", "test1");
        mapHeaders.put("key2", "test2");
        ApiConnector connector = Utility.getApiConnector(
                Constants.BASE_URL_SANDBOX ,
                "GET",
                null,
                "",
                null
        );
        HttpRequest.Builder builder = connector.setHeaders(HttpRequest.newBuilder());
        HttpRequest apiRequest = builder
                .uri(URI.create(Constants.BASE_URL_SANDBOX))
                .build();
        assertEquals(
                "GET",
                apiRequest.method()
        );
        assertEquals(
                Constants.BASE_URL_SANDBOX,
                apiRequest.uri().toString()
        );
    }

    /**
     * sets set request else part
     */
    @Test
    public void testSetRequest_GET() {
        Map<String, String> mapHeaders = new HashMap<String, String>();
        mapHeaders.put("key1", "test1");
        mapHeaders.put("key2", "test2");
        ApiConnector connector = Utility.getApiConnector(
                Constants.BASE_URL_SANDBOX ,
                "GET",
                mapHeaders,
                "",
                null
        );
        connector.setHttpRequest();
        HttpRequest apiRequest = connector.getApiRequest();
        assertEquals(
                "GET",
                apiRequest.method()
        );
        assertEquals(
                Constants.BASE_URL_SANDBOX,
                apiRequest.uri().toString()
        );
    }

    /**
     * tests set request post part
     */
    @Test
    public void testSetRequest_POST() {
        Map<String, String> mapHeaders = new HashMap<String, String>();
        mapHeaders.put("key1", "test1");
        mapHeaders.put("key2", "test2");
        ApiConnector connector = Utility.getApiConnector(
                Constants.BASE_URL_SANDBOX ,
                "POST",
                mapHeaders,
                "{}",
                null
        );
        connector.setHttpRequest();
        HttpRequest apiRequest = connector.getApiRequest();
        assertEquals(
                "POST",
                apiRequest.method()
        );
    }

    /**
     * tests send request
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testSendRequest() throws ExecutionException, InterruptedException {
        ApiConnector connector = mock(ApiConnector.class);
        HttpResponse<String> mockResp = new HttpResponse<String>() {
            @Override
            public int statusCode() {
                return 200;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
                return "{'request_id':'123123123','result':{'id':61275785, 'username':'testusername', 'projects':[{'id':12345678, 'submitdate': 1642585594}]}}";
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public HttpClient.Version version() {
                return null;
            }
        };

        CompletableFuture<HttpResponse<String>> futureResp
                = CompletableFuture.supplyAsync(() -> mockResp);
        when(connector.sendRequest()).thenReturn(
                futureResp
        );

        // callout code
        Map<String, Object> queryParamsMap = connector.getQueryParamsMap();
        queryParamsMap.put("full_description", false);
        queryParamsMap.put("sort_field", "submitdate");
        queryParamsMap.put("reverse_sort", false);
        queryParamsMap.put("owners[]", "");
        connector.setQueryParamsMap(queryParamsMap);
        connector.setEndpointURL(Constants.BASE_URL_SANDBOX + "/api/projects/0.1/projects");
        CompletionStage<String> projectsListFutureResponse
                = connector.sendRequest()
                .thenApply(HttpResponse::body);
        CompletionStage<Result> asyncResult
                = projectsListFutureResponse.thenApply(
                (respStr) ->
                {
//                    System.out.println("============================ respStr ===> \n" + respStr);
                    return (
                            (ProjectsListResponse) Utility.deserialize( respStr, "ProjectsListResponse")
                    ).result.projects;
                }
        ).thenApply(
                (lstProjects) -> ok(
                        views.html.userInfo.render(new UserDetailsResponse(), (List<Project>) lstProjects)
                )
        );

        assertEquals(OK, asyncResult.toCompletableFuture().get().status());
        System.out.println("asyncResult.toCompletableFuture().get().toString() = " + asyncResult.toCompletableFuture().get().toString());

    }

/*
    @Test
    public void testSendRequestMock() throws ExecutionException, InterruptedException {
        HttpClient client = mock(HttpClient.class);
        ApiConnector connector = new ApiConnector(client);
        HttpResponse<String> mockResp = new HttpResponse<String>() {
            @Override
            public int statusCode() {
                return 200;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
                return "{'request_id':'123123123','result':{'id':61275785, 'username':'testusername', 'projects':[{'id':12345678, 'submitdate': 1642585594}]}}";
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public HttpClient.Version version() {
                return null;
            }
        };

        CompletableFuture<HttpResponse<String>> futureResp
                = CompletableFuture.supplyAsync(() -> mockResp);
        when(client.sendAsync(connector.getApiRequest(), HttpResponse.BodyHandlers.ofString())).thenReturn(
                futureResp
        );

        // callout code
        Map<String, Object> queryParamsMap = connector.getQueryParamsMap();
        queryParamsMap.put("full_description", false);
        queryParamsMap.put("sort_field", "submitdate");
        queryParamsMap.put("reverse_sort", false);
        queryParamsMap.put("owners[]", "");
        connector.setQueryParamsMap(queryParamsMap);
        connector.setEndpointURL(Constants.BASE_URL_SANDBOX + "/api/projects/0.1/projects");
        CompletionStage<String> projectsListFutureResponse
                = connector.sendRequest()
                .thenApply(HttpResponse::body);
        CompletionStage<Result> asyncResult
                = projectsListFutureResponse.thenApply(
                (respStr) ->
                {
                    System.out.println("============================ respStr ===> \n" + respStr);
                    return (
                            (ProjectsListResponse) Utility.deserialize( respStr, "ProjectsListResponse")
                    ).result.projects;
                }
        ).thenApply(
                (lstProjects) -> ok(
                        views.html.userInfo.render(new UserDetailsResponse(), (List<Project>) lstProjects)
                )
        );

        assertEquals(OK, asyncResult.toCompletableFuture().get().status());
        System.out.println("asyncResult.toCompletableFuture().get().toString() = " + asyncResult.toCompletableFuture().get().toString());

    }

 */

}