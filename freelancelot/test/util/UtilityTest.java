package util;

import api.ApiConnector;
import jdk.jshell.execution.Util;
import models.JWTPayload;
import api.ApiConnector;
import models.JWTPayload;
import org.junit.jupiter.api.Timeout;
import play.mvc.Http;
import util.Utility;
import wrapper.ProjectsListResponse;
import wrapper.UserDetailsResponse;
import org.junit.jupiter.api.Test;
import wrapper.ProjectsListResponse;
import wrapper.UserDetailsResponse;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Utility class
 * @author Aditya Joshi
 */
class UtilityTest {

    /**
     * test method for deserialize method of Utility class
     *
     * @author Aditya Joshi
     */
    @Test
    void testDeserialize() {
        String strReqId = "testRequestId";
        String JsonToTest = "{'request_id':'testRequestId'}";
        ProjectsListResponse resp1 = (ProjectsListResponse) Utility.deserialize(JsonToTest, "ProjectsListResponse");
        assertEquals("testRequestId", resp1.request_id);
        UserDetailsResponse resp2 = (UserDetailsResponse) Utility.deserialize("{'status':'success'}", "UserDetailsResponse");
        assertEquals("success", resp2.status);
        JWTPayload resp3 = (JWTPayload) Utility.deserialize("{'nbf':123}", "JWTPayload");
        assertEquals(123, resp3.nbf);
    }

    /**
     * test method for testing conversion from integer representation of a date
     * to String formatted date
     *
     * @author Aditya Joshi
     */
    @Test
    void testConvertDate() {
        Date dateBase = new GregorianCalendar(1970, 1, 1).getTime();
        Date todayDate = Calendar.getInstance().getTime();
        long diff = todayDate.getTime() - dateBase.getTime();
        diff = TimeUnit.MILLISECONDS.toSeconds(diff);
        String outputString = Utility.convertDate((int) diff);
        assertEquals(
                new SimpleDateFormat("MMM dd, yyyy").format(todayDate),
                outputString
        );
    }

    /**
     * test method for testing conversion from integer representation of a date
     * to String formatted date
     *
     * @author Aditya Joshi
     */
    @Test
    void testCreateQueryParamString() {
        Map<String, Object> mapQueryParams = null;
        String generatedString = Utility.createQueryParamString(mapQueryParams);
        // negative test
        assertEquals("", generatedString);
        mapQueryParams = new HashMap<>();
        mapQueryParams.put("testkey", true);
        String qString = Utility.createQueryParamString(mapQueryParams);
        // positive test
        assertEquals("testkey=true", qString);
    }

    @Test
    void testGetApiConnector() {
        ApiConnector c = Utility.getApiConnector(
                "",
                "GET",
                new HashMap<String, String>(),
                "",
                new HashMap<String, Object>()
        );
        assertEquals("", c.getEndpointURL());
    }

    @Test
    public void testGetSession() {
        Http.Request request = new Http.RequestBuilder()
                .cookie(new Http.Cookie(
                        "PLAY_SESSION",
                        "eyJhbGciOiJIUzI1NiJ9.eyJkYXRhIjp7InNlc3Npb24iOiJNb3ppbGxhLzUuMCAoWDExOyBMaW51eCB4ODZfNjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS85OC4wLjQ3NTguODAgU2FmYXJpLzUzNy4zNiIsImNzcmZUb2tlbiI6ImExNzFjYWY3YjdlY2RlYmJmYWRjNzRhZTM3ZGY3YzdiNTU1ODQzZmEtMTY0OTg5Nzg1MDY3OS0yNDZiMTk0YTJkMDg4MDM5M2JkYjVhODUifSwibmJmIjoxNjQ5ODk3ODUwLCJpYXQiOjE2NDk4OTc4NTB9.XWVTz739oyRC13V3Q6i5raS84rn_B_8Pjz8lu_ETYfc",
                        null,
                        null,
                        null,
                        false,
                        false,
                        null
                        ))
                .build();

        String session = Utility.getSession(request);
        assertEquals("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.80 Safari/537.36", session);
    }

    @Test
    public void testRandomId() {
        assertFalse( Utility.getRandomId().isBlank() );
    }

    @Test
    public void testSearchQueryMethod() {
        List<String> l = new ArrayList<String>();
        l.add("test");
        Boolean b = Utility.createSearchQueryByKey("test", l).isBlank();
        assertEquals("test=test", Utility.createSearchQueryByKey("test", l));
    }
}