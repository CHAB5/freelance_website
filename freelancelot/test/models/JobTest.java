package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class Job class
 *
 * @author Aditya Joshi
 */
class JobTest {

    /**
     * test method to test constructor and getter setter
     *
     * @author Aditya Joshi
     */
    @Test
    void getName() {
        Job j = new Job();
        j.setName("testjob");
        assertEquals("testjob", j.getName());
        String testToString = "Job{" +
                "name='" + j.getName() + '\'' +
                '}';
        assertEquals(testToString, j.toString());
        Job jj = new Job("aaa");
        assertEquals("aaa", jj.getName());
    }
}