package util;

import com.google.gson.Gson;
import api.ApiConnector;
import models.JWTPayload;
import play.mvc.Http;
import wrapper.ProjectsListResponse;
import wrapper.UserDetailsResponse;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static util.Constants.INT_1_000_000;

/**
 * Utility class provides static reusable methods that are utilizable by any other classes directly.
 *
 * @author Aditya Joshi
 * @author Shubham Punekar
 */
public class Utility {

    /**
     * Method to convert string that typically represents a JSON Object to a java class instance using
     * Gson library provided by Google.
     *
     * @author Aditya Joshi
     * @param strJson string representing the JSON object
     * @param dataType data type into which strJson needs to be deserialized
     * @return Returns deserialized instance in form of Object. This return value would need to be
     *         typecast to appropriate type when using this method.
     */
    public static Object deserialize(String strJson, String dataType) {
        Object o = null;
        Gson gsonObj = new Gson();
//        System.out.println("\n\njson string received: " + strJson);
//        System.out.println("\n\ntype received: " + dataType);
        if (dataType.toLowerCase().equals("ProjectsListResponse".toLowerCase())) {
//            System.out.println("\n\n *-*-*-**- in ProjectsListResponse *-*-*-*- " );
            o = gsonObj.fromJson(
                    strJson,
                    ProjectsListResponse.class
            );
//            System.out.println(o);
        } else if (dataType.toLowerCase().equals("UserDetailsResponse".toLowerCase())) {
//            System.out.println("\n\n *-*-*-**- in UserDetailsResponse *-*-*-*- " );
            o = gsonObj.fromJson(
                    strJson,
                    UserDetailsResponse.class
            );
//            System.out.println(o);
        } else if (dataType.toLowerCase().equals("JWTPayload".toLowerCase())) {
//            System.out.println("\n\n *-*-*-**- in UserDetailsResponse *-*-*-*- " );
            o = gsonObj.fromJson(
                    strJson,
                    JWTPayload.class
            );
//            System.out.println(o);
        }
        return o;
    }

    /**
     * getter-like method to fetch new instance of ApiConnector that would
     * in turn be used to make callouts
     *
     * @author Aditya Joshi
     * @param endpointURL endpoint of the URL for the api callout
     * @param method Http method - can be GET / POST for the purpose of this project
     * @param headers headers that are set in the Http Request
     * @param body body of the API callout
     * @param queryParamsMap map representing the query parameters appended to the endpoint
     * @return new instance of ApiConnector with all passed parameters set in it
     */
    public static ApiConnector getApiConnector(
            String endpointURL,
            String method,
            Map<String, String> headers,
            String body,
            Map<String, Object> queryParamsMap
    ) {
        return new ApiConnector(
                endpointURL,
                method,
                headers,
                body,
                queryParamsMap
        );
    }


    /**
     * Generates a query param string from the Map that is supplied to it. For example, the output looks
     * like key1=value1&key2=value2 .
     *
     * @author Aditya Joshi
     * @param mapOfQueryParams map of query param name and its value. Value can be of any type in Java environment but
     *                         while forming the query string it is cast to String.
     * @return string representing all query parameters
     */
    public static String createQueryParamString(Map<String, Object> mapOfQueryParams) {
        String queryStr = "";
        if (mapOfQueryParams != null) {
            queryStr = mapOfQueryParams.keySet()
                    .stream()
                    .map((element) -> element + "=" + String.valueOf(mapOfQueryParams.get(element)))
                    .collect(Collectors.joining("&"));
        }
        return queryStr;
    }

    /**
     * Firstly, it converts timestamp into number of days passed between 1st Jan 1970 to
     * the date that the timestamp represents. Further, the calculated number of days
     * are added to 1st Jan 1970 to get the actual date and then this date is formatted
     * in MMM dd, yyyy format and is returned.
     *
     * @author Aditya Joshi
     * @param timestamp seconds passed between the date in the question and 1st Jan 1970
     * @return text form of date
     */
    public static String convertDate(int timestamp) {
        String dateString = "";
        if (timestamp > 0) {
            int numberOfDaysPassed = ((timestamp / 60) / 60) / 24;
            Calendar c = new GregorianCalendar(1970, 1, 1);
            c.add(Calendar.DAY_OF_MONTH, numberOfDaysPassed);
            dateString = new SimpleDateFormat("MMM dd, yyyy")
                    .format(
                            c.getTime()
                    );
        }
        return dateString;
    }

/**
 * Retrieves the session information stored in the jwt token sent by client in the PLAY_SESSION cookie
 *
 * @author Shubham Punekar
 * @param request Http request sent by client, containing cookie used to determine session
 * @return session information, encapsulating the User-Agent of the client (to distinguish browsers)
 */
    public static String getSession(Http.Request request) {
        // On index page, the user agent is passed back to the client as session data, a JWT token is supplied
        // by the client on every subsequent http request which encapsulates this user agent and 2 time params - iat, nbf


        // Simulating LOGIN based flow -> Every time user starts with the index page, this is treated as a fresh login
        // For this, the entire jwt token is used as a key in the search queries map (token contains user agent + time)
        // String session = jwt;

        // Distinguish only on the basis of the browsers -> get the value of the session key in the jwt token
        String jwt = request.getCookie("PLAY_SESSION").get().value();
        String[] chunks = jwt.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        String session = ((JWTPayload) Utility.deserialize(payload, "JWTPayload")).data.session;

        return session;
    }

    /**
     * Generates a string with random integer
     *
     * @author Aditya Joshi
     * @return string formed by a random number
     */
    public static String getRandomId() {
        return (
                String.valueOf(new Random().nextInt(INT_1_000_000))
                        + "_" +
                        new SimpleDateFormat("MMyyyyddHHmmss").format(new Date())
        );
    }

    /**
     * Create a string which represents a search query that gets appended to endpoint URL
     *
     * @param key key used for query string params
     * @param lstValues values used for the key
     * @return string which represents a search query that gets appended to endpoint URL
     */
    public static String createSearchQueryByKey(String key, List<String> lstValues) {
        return lstValues.stream()
                .map(
                        value -> (key + "=" + value)
                )
                .collect(Collectors.joining("&"));
    }



}