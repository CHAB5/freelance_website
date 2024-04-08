package models;

import util.Utility;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Map;

/**
 * Container class for metadata for API Callout like method, endpoint url etc.
 *
 * @author Aditya Joshi
 */
public class ApiMetadata {
    HttpClient httpClient;
    String endpointURL;
    String method;
    Map<String, String> headers;
    String body;
    Map<String, Object> queryParamsMap;
    HttpRequest apiRequest;

    /**
     * Parameterized constructor
     *
     * @author Aditya Joshi
     * @param endpointURL endpoint url for API
     * @param method method of API callout
     * @param headers headers for API callout
     * @param body API callout body
     * @param queryParamsMap map of key value pair for query params
     */
    public ApiMetadata(String endpointURL, String method, Map<String, String> headers, String body, Map<String, Object> queryParamsMap) {
        this.endpointURL = endpointURL;
        this.method = method;
        this.headers = headers;
        this.body = body;
        this.queryParamsMap = queryParamsMap;
        System.out.println("---1");
        System.out.println(endpointURL);
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        System.out.println("---2");
        this.setHttpRequest();
        System.out.println(
                "\nMethod = " + getApiRequest().method()
                        +
                "\nURI = " + getApiRequest().uri()
        );
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
    private HttpRequest.Builder setHeaders(HttpRequest.Builder builder) {
        if (this.headers != null) {
            this.headers.keySet()
                    .stream()
                    .forEach(
                            (key) -> builder.headers(key, this.headers.get(key))
                    );
        }
        return builder;
    }

    /**
     * getter method for apiRequest member variable
     *
     * @author Aditya Joshi
     * @return api request instance
     */
    public HttpRequest getApiRequest() {
        return this.apiRequest;
    }

    /**
     * method to set new HttpRequest instance with appropriate parameters
     *
     * @author Aditya Joshi
     * @return request instance with method, body and endpoint set in it
     */
    private void setHttpRequest() {
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

    public String getEndpointURL() {
        return endpointURL;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public Map<String, Object> getQueryParamsMap() {
        return queryParamsMap;
    }

    public void setEndpointURL(String endpointURL) {
        this.endpointURL = endpointURL;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setQueryParamsMap(Map<String, Object> queryParamsMap) {
        this.queryParamsMap = queryParamsMap;
    }
}
