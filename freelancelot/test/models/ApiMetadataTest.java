package models;

import org.junit.jupiter.api.Test;
import util.Constants;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test class for ApiMetadata class
 *
 * @author Aditya Joshi
 */
class ApiMetadataTest {

    /**
     * tests getter setters
     * @author Aditya Joshi
     */
    @Test
    void testAllGettersSetters() {
        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("test", "test");
        ApiMetadata metadata2 = new ApiMetadata(
                Constants.BASE_URL_PROD + "/api/projects/0.1/projects",
                "GET",
                testMap,
                "",
                null
        );
        assertNotEquals(null, metadata2.getHttpClient());
        assertNotEquals(null, metadata2.getApiRequest());
        assertEquals(Constants.BASE_URL_PROD + "/api/projects/0.1/projects", metadata2.getEndpointURL());
        assertEquals("", metadata2.getBody());
        assertEquals("GET", metadata2.getMethod());
        assertEquals(null, metadata2.getQueryParamsMap());
        assertEquals(1, metadata2.getHeaders().size());
        metadata2.setBody("test");
        metadata2.setEndpointURL("test");
        metadata2.setHeaders(null);
        metadata2.setMethod("POST");
        metadata2.setQueryParamsMap(new HashMap<>());
        assertEquals("test", metadata2.getEndpointURL());
        assertEquals("test", metadata2.getBody());
        assertEquals("POST", metadata2.getMethod());
        assertEquals(0, metadata2.getQueryParamsMap().size());
        assertEquals(null, metadata2.getHeaders());

    }

}