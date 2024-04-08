package actors.services.descriptionreadability;

import models.Project;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.Objects.requireNonNull;

/**
 * Container class for messages to be processed by the DescriptionReadabilityActory
 * @author Shubham Punekar
 */
public class DescriptionReadabilityProtocol {

    public static Class<? extends Object> IndicesCommand;

    /**
     * To inititate the computation of readability indices for given message.
     * @author Shubham Punekar
     */
    public static class IndicesCommand {
        private Integer projectID;
        private String projectTitle;
        private String projectPreviewDescription;

        /**
         * Constructor for the command
         * @param projectID project id fetched from freelancer
         * @param projectTitle project title fetched from freelancer
         * @param projectPreviewDescription project preview description fetched from freelancer
         */
        public IndicesCommand(Integer projectID, String projectTitle, String projectPreviewDescription) {
            this.projectID = projectID;
            this.projectTitle = projectTitle;
            this.projectPreviewDescription = projectPreviewDescription;
        }

        /**
         * Accessor for project ID
         * @return project ID
         */
        public Integer getProjectID() {
            return projectID;
        }

        /**
         * Accessor for project title
         * @return project title
         */
        public String getProjectTitle() {
            return projectTitle;
        }

        /**
         * Accessor for project preview description
         * @return project preview description
         */
        public String getProjectPreviewDescription() {
            return projectPreviewDescription;
        }
    }

    /**
     * To inititate the computation of readability indices for given projects.
     * @author Shubham Punekar
     */
    public static class AverageIndicesCommand {
        CompletionStage<List<Project>> asyncListOfProjects;
        String searchString;

        /**
         * Constructor for the command
         * @param asyncListOfProjects A completion stage wrapping the list of projects
         * @param searchString Query fetched from freelancer
         */
        public AverageIndicesCommand(CompletionStage<List<Project>> asyncListOfProjects, String searchString) {
            this.asyncListOfProjects = requireNonNull(asyncListOfProjects);
            this.searchString = requireNonNull(searchString);
        }

        /**
         * Accessor for completion stage wrapping list of projects
         * @return completion stage wrapping list of projects
         */
        public CompletionStage<List<Project>> getAsyncListOfProjects() {
            return asyncListOfProjects;
        }

        /**
         * Accessor for the query
         * @return query
         */
        public String getSearchString() {
            return searchString;
        }
    }
}
