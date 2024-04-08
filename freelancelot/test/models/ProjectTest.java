package models;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Project class
 *
 * @author Aditya Joshi
 */
class ProjectTest {

    /**
     * tests getters and setters of Project class variables
     *
     * @author Aditya Joshi
     */
    @Test
    void testGetterSetters() {
        Project p = new Project();
        p.type = "";
        p.jobs = new ArrayList<Job>();
        p.submitdate = 1;
        assertEquals(0, p.getJobs().size());
        assertEquals("", p.getType());
        assertEquals(1, p.getSubmitdate());

    }
}