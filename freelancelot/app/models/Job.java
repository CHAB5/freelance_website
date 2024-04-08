package models;

/**
 * @author Harsheen Kaur
 * Mapping class for skills in project list
 * Used to get the skills present in the project list under the field Jobs in Project
 *
 */
public class Job {

    public String name;

    /**
     * Parameterized Constructor
     *
     * @author Harsheen Kaur
     * @param name name of job
     */
    public Job(String name) {
        this.name = name;
    }

    /**
     * Default Constructor
     *
     * @author Harsheen Kaur
     */
    public Job() {}

    /**
     * Getter method for name variable
     * @return name of job
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name variable
     *
     * @author Harsheen Kaur
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * toString method implementation
     *
     * @author Harsheen Kaur
     * @return string representing Job class
     */
    @Override
    public String toString() {
        return "Job{" +
                "name='" + name + '\'' +
                '}';
    }
}
