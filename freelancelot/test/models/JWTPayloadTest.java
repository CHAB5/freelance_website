package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for JWTPayload
 *
 * @author Aditya Joshi
 */
class JWTPayloadTest {
    /**
     * tests Constructor of JWTPayload class
     *
     * @author Aditya Joshi
     */
    @Test
    void testConstructor() {
        JWTPayload p = new JWTPayload();

        assertNotEquals(null, p);

        assertEquals(null, p.data);
    }
}