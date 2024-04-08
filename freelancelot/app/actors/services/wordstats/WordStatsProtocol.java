package actors.services.wordstats;

import models.Project;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.Objects.requireNonNull;

/**
 * Container class for messages to be processed by the WordStatsActor.
 *
 * @author Chandana Basavaraj
 */
public class WordStatsProtocol {

    /**
     * To initiate the computation of projectPreviewDescription for given message.
     *
     * @author Chandana Basavaraj
     */
    public static class IndividualStatsCommand {
        private String projectPreviewDescription;

        /**
         * Parameterized constructor for the command
         *
         * @author Chandana Basavaraj
         * @param projectPreviewDescription
         */
        public IndividualStatsCommand(String projectPreviewDescription) {
            this.projectPreviewDescription = projectPreviewDescription;
        }

        /**
         * Getter for project Preview Description
         *
         * @author Chandana Basavaraj
         * @return project Preview Description
         */
        public String getProjectPreviewDescription() {
            return projectPreviewDescription;
        }
    }

    /**
     * To initiate the computation of projectPreviewDescription for a list of projects
     * for given message.
     *
     * @author Chandana Basavaraj
     */
    public static class GlobalStatsCommand {
        CompletionStage<List<Project>> asyncListOfProjects;

        /**
         * Parameterized constructor for the command
         *
         * @author Chandana Basavaraj
         * @param asyncListOfProjects
         */
        public GlobalStatsCommand(CompletionStage<List<Project>> asyncListOfProjects) {
            this.asyncListOfProjects = requireNonNull(asyncListOfProjects);
        }

        /**
         * Getter for async List Of Projects
         *
         * @author Chandana Basavaraj
         * @return asyncListOfProjects
         */
        public CompletionStage<List<Project>> getAsyncListOfProjects() {
            return asyncListOfProjects;
        }
    }
}
