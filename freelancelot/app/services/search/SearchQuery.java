package services.search;

import api.ApiConnector;

/**
 * @author Shubham Punekar
 * Class to initialise and get the parameters of search query
 */
public class SearchQuery {
    String searchString;

    public SearchQuery() {};

    /**
     *
     * @param searchString - query
     */
    public SearchQuery(String searchString) {
        this.searchString = searchString;
    }

    /**
     *
     * @return - searched string
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     *
     * @param searchString - sets the value of the searched string
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}