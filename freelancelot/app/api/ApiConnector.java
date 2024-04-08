package api;

import util.Utility;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * ApiConnector class creates an HTTP connection that is used in the API callouts that are made inside makeCallout method.
 *
 * @author Aditya Joshi
 */
public class ApiConnector {

    private HttpClient httpClient;
    String endpointURL;
    String method;
    Map<String, String> headers;
    String body;
    Map<String, Object> queryParamsMap;

    /**
     * getter method for apiRequest member variable
     *
     * @author Aditya Joshi
     * @return returns apiRequest member variable of this class
     */
    public HttpRequest getApiRequest() {
        return apiRequest;
    }

    HttpRequest apiRequest;
    /**
     * default constructor
     *
     * @author Aditya Joshi
     * @version 1.0
     */
    public ApiConnector() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    /**
     * parameterized constructor
     *
     * @author Aditya Joshi
     * @param httpClient client instance to set private instance httpClient of this class
     */
    public ApiConnector(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * parameterized constructor
     *
     * @author Aditya Joshi
     * @param endpointURL URL endpoint of the API callout
     * @param method HTTP method used for API callout (GET, POST, PATCH, PUT, DELETE etc.)
     * @param headers map representing API request headers
     * @param body string representing API callout body
     * @param queryParamsMap map from which the query param string should be created. The resulting string
     *                       is appended to the endpoint URL at the time of callout.
     */
    public ApiConnector(String endpointURL, String method, Map<String, String> headers, String body, Map<String, Object> queryParamsMap) {
        this.endpointURL = endpointURL;
        this.method = method;
        this.headers = headers;
        this.body = body;
        this.queryParamsMap = queryParamsMap;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    /**
     * getter for HttpClient instance present in the class
     *
     * @author Aditya Joshi
     * @return httpClient member variable of the class
     */
    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    /**
     * sets the headers for the builder instance passed to it and returns the same
     *
     * @author Aditya Joshi
     * @param builder instance of HttpRequest.Builder on which the headers are set
     * @return builder with headers set on it
     */
    public HttpRequest.Builder setHeaders(HttpRequest.Builder builder) {
        if (this.headers != null) {
//            for (String key : this.headers.keySet()) {
//                builder.header(key, this.headers.get(key));
//            }
            this.headers.keySet()
                    .stream()
                    .forEach(
                            (key) -> builder.headers(key, this.headers.get(key))
                    );
        }
        return builder;
    }

    /**
     * method to set new HttpRequest instance with appropriate parameters
     *
     * @author Aditya Joshi
     * @return request instance with method, body and endpoint set in it
     */
    public void setHttpRequest() {
        HttpRequest.Builder builder = setHeaders(HttpRequest.newBuilder());
        if (this.method.toLowerCase().trim().equals("get")) {
            builder = builder.GET();
        } else if (this.method.toLowerCase().trim().equals("post")) {
            builder = builder.POST(HttpRequest.BodyPublishers.ofString(body));
        }
        String queryParamsString = Utility.createQueryParamString(queryParamsMap);
        String apiEndpointUrl = this.endpointURL;
        if (!queryParamsString.isBlank()) {
            apiEndpointUrl += ("?" + queryParamsString);
        }
//        System.out.println("apiEndpointUrl ========= " + apiEndpointUrl);
        apiRequest = builder
                .uri(URI.create(apiEndpointUrl))
                .build();
    }

    /**
     * makes an api callout using endpoint, headers, method, query params, body as specified in the constructor
     *
     * @author Aditya Joshi
     * @return CompletionStage for the response
     */
    public CompletionStage<HttpResponse<String>> sendRequest() {
        this.setHttpRequest();
        return getHttpClient().sendAsync(
                this.apiRequest,
                HttpResponse.BodyHandlers.ofString()
        );
    }

    /**
     * getter for map of query params
     *
     * @author Aditya Joshi
     * @return gets the map of query params
     */
    public Map<String, Object> getQueryParamsMap() {
        return queryParamsMap;
    }

    /**
     * setter for endpoint url
     *
     * @author Aditya Joshi
     * @param queryParamsMap
     */
    public void setQueryParamsMap(Map<String, Object> queryParamsMap) {
        this.queryParamsMap = queryParamsMap;
    }

    /**
     * getter for endpoint URL of API callout
     *
     * @author Aditya Joshi
     * @return gets the endpoint URL
     */
    public String getEndpointURL() {
        return endpointURL;
    }

    /**
     * setter for endpoint url
     *
     * @author Aditya Joshi
     * @param endpointURL
     */
    public void setEndpointURL(String endpointURL) {
        this.endpointURL = endpointURL;
    }
}



