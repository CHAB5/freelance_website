package wrapper;

import java.util.ArrayList;

/**
 * UserDetailsResponse is a POJO class representing response for User API.\
 *
 * @author Aditya Joshi
 */
public class UserDetailsResponse {

    public String status;
    public Result result;
    public String request_id;

    /**
     * parameterized constructor
     *
     * @author Aditya Joshi
     * @param status
     * @param result
     * @param request_id
     */
    public UserDetailsResponse(String status, Result result, String request_id) {
        this.status = status;
        this.result = result;
        this.request_id = request_id;
    }

    /**
     * default constructor
     *
     * @author Aditya Joshi
     */
    public UserDetailsResponse() {
        this.status = "";
        this.result = new Result();
        this.request_id = "";
    }

    public class CategoryRatings{
        public double quality;
        public double communication;
        public double expertise;
        public double professionalism;
        public double hire_again;
        public double clarity_spec;
        public double payment_prom;
        public double work_for_again;
    }

    public class EntireHistory{
        public double overall;
        public double on_budget;
        public double on_time;
        public double positive;
        public CategoryRatings category_ratings;
        public int all;
        public int reviews;
        public int incomplete_reviews;
        public int complete;
        public int incomplete;
        public Object earnings;
        public double completion_rate;
        public Object rehire_rate;
        public int user_id;
        public Object completed_relevant_job_count;
    }

    public class Last3months{
        public double overall;
        public double on_budget;
        public double on_time;
        public double positive;
        public CategoryRatings category_ratings;
        public int all;
        public int reviews;
        public int incomplete_reviews;
        public int complete;
        public int incomplete;
        public Object earnings;
        public double completion_rate;
        public Object rehire_rate;
        public int user_id;
        public Object completed_relevant_job_count;
    }

    public class Last12months{
        public double overall;
        public double on_budget;
        public double on_time;
        public double positive;
        public CategoryRatings category_ratings;
        public int all;
        public int reviews;
        public int incomplete_reviews;
        public int complete;
        public int incomplete;
        public Object earnings;
        public double completion_rate;
        public Object rehire_rate;
        public int user_id;
        public Object completed_relevant_job_count;
    }

    public class JobHistory{
        public int count_other;
        public ArrayList<Object> job_counts;
    }

    public class Reputation{
        public EntireHistory entire_history;
        public Last3months last3months;
        public Last12months last12months;
        public int user_id;
        public String role;
        public double earnings_score;
        public JobHistory job_history;
        public Object project_stats;
    }

    public class Country{
        public String name;
        public String flag_url;
        public String code;
        public String highres_flag_url;
        public String flag_url_cdn;
        public String highres_flag_url_cdn;
        public Object iso3;
        public Object region_id;
        public Object phone_code;
        public Object demonym;
        public Object person;
        public Object seo_url;
        public Object sanction;
        public Object language_code;
        public Object language_id;
    }

    public class Location{
        public Country country;
        public String city;
        public double latitude;
        public double longitude;
        public Object vicinity;
        public Object administrative_area;
        public Object full_address;
        public Object administrative_area_code;
        public Object postal_code;
    }

    public class Category{
        public int id;
        public String name;
    }

    public class Job{
        public int id;
        public String name;
        public Category category;
        public Object active_project_count;
        public String seo_url;
        public Object seo_info;
        public boolean local;
    }

    public class JobCount{
        public int count;
        public Job job;
    }

    public class ProjectStats{
        public int myopen;
        public int work_in_progress;
        public int complete;
        public int pending;
        public int draft;
    }

    public class EmployerReputation{
        public EntireHistory entire_history;
        public Last3months last3months;
        public Last12months last12months;
        public int user_id;
        public String role;
        public double earnings_score;
        public JobHistory job_history;
        public ProjectStats project_stats;
    }

    public class Status{
        public boolean payment_verified;
        public boolean email_verified;
        public boolean deposit_made;
        public boolean profile_complete;
        public boolean phone_verified;
        public boolean identity_verified;
        public boolean facebook_connected;
        public boolean freelancer_verified_user;
        public boolean linkedin_connected;
    }

    public class PrimaryCurrency{
        public int id;
        public String code;
        public String sign;
        public String name;
        public double exchange_rate;
        public String country;
        public boolean is_external;
        public boolean is_escrowcom_supported;
    }

    public class Context{
        public int id;
        public String type;
    }

    public class CurrentImage{
        public Object id;
        public String url;
        public String description;
        public int width;
        public int height;
        public Context context;
    }

    public class CoverImage{
        public CurrentImage current_image;
        public Object past_images;
    }

    public class Timezone{
        public int id;
        public String country;
        public String timezone;
        public double offset;
    }

    public class Result{
        public int id;
        public String username;
        public Object suspended;
        public boolean closed;
        public boolean is_active;
        public Object force_verify;
        public String avatar;
        public String email;
        public Reputation reputation;
        public ArrayList<Object> jobs;
        public String profile_description;
        public Object hourly_rate;
        public int registration_date;
        public boolean limited_account;
        public String display_name;
        public Object tagline;
        public Location location;
        public String avatar_large;
        public String role;
        public String chosen_role;
        public EmployerReputation employer_reputation;
        public Status status;
        public String avatar_cdn;
        public String avatar_large_cdn;
        public PrimaryCurrency primary_currency;
        public Object account_balances;
        public Object membership_package;
        public Object qualifications;
        public Object badges;
        public String primary_language;
        public Object search_languages;
        public CoverImage cover_image;
        public Object true_location;
        public Object portfolio_count;
        public boolean preferred_freelancer;
        public Object support_status;
        public String first_name;
        public String last_name;
        public Object mobile_tracking;
        public Object corporate;
        public String public_name;
        public Object corporate_users;
        public boolean is_local;
        public Object address;
        public Object company;
        public Object recommendations;
        public ArrayList<Object> pool_ids;
        public ArrayList<Object> enterprise_ids;
        public Object date_of_birth;
        public Object escrowcom_account_linked;
        public boolean escrowcom_interaction_required;
        public Object marketing_mobile_number;
        public ArrayList<Object> user_sanctions;
        public Object freelancer_verified_status;
        public Object secure_phone;
        public Object directory_follow_preferences;
        public boolean is_payment_confirmation_required;
        public boolean is_nominated_payment_controlled;
        public boolean is_nominated_payment_share_setup_complete;
        public Object spam_profile;
        public Object endorsements;
        public Timezone timezone;
        public Object responsiveness;
        public Object bid_quality_details;
        public Object test_user;
        public Object online_offline_status;
        public Object deposit_methods;
        public boolean oauth_password_credentials_allowed;
        public boolean registration_completed;
        public boolean is_profile_visible;
        public Object enterprise_metadata_values;
        public Object mfa;
        public Object grants;
        public Object operating_areas;
        public Object equipment_groups;
    }

    /**
     * toString method for UserDetailsResponse class
     *
     * @author Aditya Joshi
     * @return String representation of the UserDetailsResponse instance
     */
    @Override
    public String toString() {
        return "My name is " + (String)this.result.first_name + " " + (String)this.result.last_name + ". My role is " + this.result.role;
    }
}
