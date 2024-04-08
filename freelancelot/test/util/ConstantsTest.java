package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConstantsTest {
    @Test
    public void testConstants() {
        int limit = (int) Constants.QUERY_PARAM_EMPLOYER_INFO.get("limit");
        assertEquals(Constants.NUM_OF_PROJECTS_ON_EMPLOYER_PROFILE, limit);
    }
}