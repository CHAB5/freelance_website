package actors.services.descriptionreadability;

import akka.actor.AbstractActor;
import akka.actor.Props;
import models.Project;
import services.descriptionreadability.DescriptionReadability;

import java.util.List;
import java.util.Objects;

/**
 *Helper for DescriptionReadability calculation using Akka
 *
 * @author Shubham Punekar
 */
public class DescriptionReadabilityActor extends AbstractActor {
    public DescriptionReadabilityActor() {
    }

    /**
     * get properties for creating an Actor
     *
     * @author Shubham Punekar
     * @return
     */
    public static Props getProps() {
        return Props.create(DescriptionReadabilityActor.class);
    }

    /**
     * Static container class for DescriptionReadability Actor
     * with attributes for average indices for a list of projects.
     *
     * @author Shubham Punekar
     */
    public static class AverageIndicesResult {
        private String searchString;
        private double averageFREI;
        private double averageFKGL;
        private List<Project> listOfProjects;

        /**
         * parameterized constructor for AverageIndicesResult
         *
         * @author Shubham Punekar
         * @param searchString
         * @param averageFREI
         * @param averageFKGL
         * @param listOfProjects
         */
        public AverageIndicesResult(String searchString, double averageFREI, double averageFKGL, List<Project> listOfProjects) {
            this.searchString = searchString;
            this.averageFREI = averageFREI;
            this.averageFKGL = averageFKGL;
            this.listOfProjects = listOfProjects;
        }

        /**
         * gets the search string
         *
         * @author Shubham Punekar
         * @return search String
         */
        public String getSearchString() {
            return searchString;
        }

        /**
         * gets the AverageFREI
         *
         * @author Shubham Punekar
         * @return averageFREI
         */
        public double getAverageFREI() {
            return averageFREI;
        }

        /**
         * gets the AverageFKGL
         *
         * @author Shubham Punekar
         * @return averageFKGL
         */
        public double getAverageFKGL() {
            return averageFKGL;
        }

        /**
         * gets the list of projects
         *
         * @author Shubham Punekar
         * @return listOfProjects
         */
        public List<Project> getListOfProjects() {
            return listOfProjects;
        }

        /**
         * Returns String format of AverageIndicesResult object
         *
         * @author Shubham Punekar
         * @return AverageIndicesResult
         */
        @Override
        public String toString() {
            return "AverageIndicesResult{" +
                    "searchString='" + searchString + '\'' +
                    ", averageFREI=" + averageFREI +
                    ", averageFKGL=" + averageFKGL +
                    '}';
        }

        /**
         * compares if the passed object is equal to invoking object
         * based on the values of averageFREI and averageFKGL
         *
         * @author Shubham Punekar
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AverageIndicesResult that = (AverageIndicesResult) o;
            return Double.compare(that.getAverageFREI(), getAverageFREI()) == 0 && Double.compare(that.getAverageFKGL(), getAverageFKGL()) == 0 && Objects.equals(getSearchString(), that.getSearchString()) && Objects.equals(getListOfProjects(), that.getListOfProjects());
        }
    }

    /**
     * Static container class for DescriptionReadability Actor
     * with attributes for the individual project.
     *
     * @author Shubham Punekar
     */
    public static class IndicesResult {
        private Integer projectID;
        private String projectTitle;
        private String projectPreviewDescription;

        /**
         * parameterized constructor for IndicesResult
         *
         * @author Shubham Punekar
         * @param projectID
         * @param projectTitle
         * @param projectPreviewDescription
         */
        public IndicesResult(Integer projectID, String projectTitle, String projectPreviewDescription) {
            this.projectID = projectID;
            this.projectTitle = projectTitle;
            this.projectPreviewDescription = projectPreviewDescription;
        }

        /**
         * gets projectID
         *
         * @author Shubham Punekar
         * @return project ID
         */
        public Integer getProjectID() {
            return projectID;
        }

        /**
         * gets projectTitle
         *
         * @author Shubham Punekar
         * @return project Title
         */
        public String getProjectTitle() {
            return projectTitle;
        }

        /**
         * gets projectPreviewDescription
         *
         * @author Shubham Punekar
         * @return project Preview Description
         */
        public String getProjectPreviewDescription() {
            return projectPreviewDescription;
        }

        /**
         * compares if the passed object is equal to invoking object
         * based on the values projectID
         *
         * @author Shubham Punekar
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IndicesResult that = (IndicesResult) o;
            return Objects.equals(getProjectID(), that.getProjectID()) && Objects.equals(getProjectTitle(), that.getProjectTitle()) && Objects.equals(getProjectPreviewDescription(), that.getProjectPreviewDescription());
        }

    }

    /**
     * This method defines which two messages the DescriptionReadability Actor can handle for individual project indices
     * and average indices, along with the implementation of these messages received are handled for the respective indices
     * processing. These behaviors are built with a builder named ReceiveBuilder.
     *
     * @author Shubham Punekar
     * @return Receive
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DescriptionReadabilityProtocol.IndicesCommand.class, command -> {
                    sender().tell(new IndicesResult(command.getProjectID(), command.getProjectTitle(), command.getProjectPreviewDescription()), self());
                })
                .match(DescriptionReadabilityProtocol.AverageIndicesCommand.class, command -> {
                    var sender = sender(); // effectively final copy of the variable
//                    System.out.println("BEFORE ASYNC : " + sender.path().toString());
                    command.getAsyncListOfProjects().thenAccept(
                            listOfProjects -> {
//                                System.out.println("IN ASYNC BEGIN: " + sender.path().toString());
                                int count = listOfProjects
                                        .stream()
                                        .map(project -> 1)
                                        .mapToInt(Integer::valueOf)
                                        .sum();
                                double averageFREI = 0;
                                double averageFKGL = 0;
                                if (count != 0) {
                                    averageFREI = listOfProjects
                                            .stream()
                                            .map(project -> project.preview_description)
                                            .map(DescriptionReadability::computeFleschReadabilityIndex)
                                            .mapToDouble(Double::doubleValue)
                                            .sum();
                                    averageFKGL = listOfProjects
                                            .stream()
                                            .map(project -> project.preview_description)
                                            .map(DescriptionReadability::computeFleschKincaidGradeLevel)
                                            .mapToDouble(Double::doubleValue)
                                            .sum();
                                    averageFREI /= count;
                                    averageFKGL /= count;
                                }
                                var returnClassObj = new AverageIndicesResult(command.getSearchString(), averageFREI, averageFKGL, listOfProjects);
//                                    System.out.println("IN DESCRIPTION READABILITY ACTOR: ");
//                                    System.out.println(returnClassObj);
//                                System.out.println("IN ASYNC END: " + sender.path().toString());
                                sender.tell(returnClassObj, self());
                            }
                    );

                })
                .build();
    }

}
