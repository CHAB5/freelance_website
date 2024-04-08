package util;

import java.util.HashMap;

/**
 * Class to hold all constants and magic numbers used in the project
 * @author Aditya Joshi
 * @author Shubham Punekar
 * @author Harsheen Kaur
 * @author Chandana Basavaraj
 */
public class Constants {
    public static int INT_1_000_000 = 1000000;
    public static int LIMIT_SEARCH_QUERIES = 3;
    public static int LIMIT_PROJECTS_PER_QUERY = 10;
    public static final String BASE_URL_SANDBOX = "https://www.freelancer-sandbox.com";
    public static final String BASE_URL_PROD = "https://www.freelancer.com";
    public static final String URL_PROJECTS_BY_EMPLOYER_ID = BASE_URL_SANDBOX + "/api/projects/0.1/projects" ;
    public static int NUM_OF_PROJECTS_ON_EMPLOYER_PROFILE = 10;
    public static HashMap<String, Object> BASIC_QUERY_PARAM_MAP = new HashMap<>();
    public static HashMap<String, Object> QUERY_PARAM_EMPLOYER_INFO = new HashMap<>();
    static {
        BASIC_QUERY_PARAM_MAP.put("compact", false);
        BASIC_QUERY_PARAM_MAP.put("full_description", true);

        QUERY_PARAM_EMPLOYER_INFO.putAll(BASIC_QUERY_PARAM_MAP);
        QUERY_PARAM_EMPLOYER_INFO.put("limit", NUM_OF_PROJECTS_ON_EMPLOYER_PROFILE);
        QUERY_PARAM_EMPLOYER_INFO.put("sort_field", "submitdate");
        QUERY_PARAM_EMPLOYER_INFO.put("reverse_sort", false);
        QUERY_PARAM_EMPLOYER_INFO.put("avatar", true);
        QUERY_PARAM_EMPLOYER_INFO.put("country_details", true);
        QUERY_PARAM_EMPLOYER_INFO.put("profile_description", true);
        QUERY_PARAM_EMPLOYER_INFO.put("display_info", true);
        QUERY_PARAM_EMPLOYER_INFO.put("jobs", true);
        QUERY_PARAM_EMPLOYER_INFO.put("location_details", true);
        QUERY_PARAM_EMPLOYER_INFO.put("preferred_details", true);
        QUERY_PARAM_EMPLOYER_INFO.put("status", true);
        QUERY_PARAM_EMPLOYER_INFO.put("reputation", true);
        QUERY_PARAM_EMPLOYER_INFO.put("employer_reputation", true);
        QUERY_PARAM_EMPLOYER_INFO.put("reputation_extra", true);
        QUERY_PARAM_EMPLOYER_INFO.put("employer_reputation_extra", true);
        QUERY_PARAM_EMPLOYER_INFO.put("cover_image", true);
        QUERY_PARAM_EMPLOYER_INFO.put("mobile_tracking", true);
        QUERY_PARAM_EMPLOYER_INFO.put("deposit_methods", true);
        QUERY_PARAM_EMPLOYER_INFO.put("user_recommendations", true);
        QUERY_PARAM_EMPLOYER_INFO.put("marketing_mobile_number", true);
        QUERY_PARAM_EMPLOYER_INFO.put("sanction_details", true);

    }
}
