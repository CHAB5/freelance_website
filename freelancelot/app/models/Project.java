package models;

import wrapper.ProjectsListResponse;

import java.util.ArrayList;

/**
 * POJO class representing Project entity of Freelancer.com
 *
 * @author Aditya Joshi
 */
public class Project {
    public int id;
    public int owner_id;
    public String title;
    public String status;
    public String sub_status;
    public String seo_url;
    public ProjectsListResponse.Currency currency;
    public Object description;
    public ArrayList<Job> jobs;     // TODO: List of Jobs (create a new Job class in ProjectListResponse)
    public int submitdate;
    public String strDateValue;
    public String preview_description;
    public boolean deleted;
    public boolean nonpublic;
    public boolean hidebids;
    public String type;
    public int bidperiod;
    public ProjectsListResponse.Budget budget;
    public ProjectsListResponse.HourlyProjectInfo hourly_project_info;
    public boolean featured;
    public boolean urgent;
    public Object assisted;
    public Object active_prepaid_milestone;
    public ProjectsListResponse.BidStats bid_stats;
    public int time_submitted;
    public long time_updated;
    public ProjectsListResponse.Upgrades upgrades;
    public Object qualifications;
    public String language;
    public Object attachments;
    public boolean hireme;
    public Object hireme_initial_bid;
    public Object invited_freelancers;
    public Object recommended_freelancers;
    public String frontend_project_status;
    public Object nda_signatures;
    public ProjectsListResponse.Location location;
    public Object true_location;
    public boolean local;
    public boolean negotiated;
    public Object negotiated_bid;
    public int time_free_bids_expire;
    public Object can_post_review;
    public Object files;
    public Object user_distance;
    public Object from_user_location;
    public Object project_collaborations;
    public Object support_sessions;
    public Object track_ids;
    public Object drive_files;
    public Object nda_details;
    public ArrayList<String> pool_ids;
    public ArrayList<Integer> enterprise_ids;
    public Object timeframe;
    public Object deloitte_details;
    public boolean is_escrow_project;
    public boolean is_seller_kyc_required;
    public boolean is_buyer_kyc_required;
    public Object local_details;
    public Object equipment;
    public Object nda_signatures_new;
    public Object billing_code;
    public ArrayList<Object> enterprise_metadata_values;
    public ProjectsListResponse.ProjectRejectReason project_reject_reason;
    public Object repost_id;
    public Object client_engagement;
    public Object contract_signatures;
    public Object quotation_id;
    public Object quotation_version_id;
    public Object enterprise_linked_projects_details;
    public Object equipment_groups;
    public Object project_source;
    public Object project_source_reference;
    public double flesch_readability_index;
    public double flesch_kincaid_grade_level;

    /**
     * getter for jobs member variable
     *
     * @author Harsheen Kaur
     * @return List of jobs(skills) in the project
     */
    public ArrayList<Job> getJobs() {
        return jobs;
    }

    /**
     * getter for type member variable
     *
     * @author Aditya Joshi
     * @return type of project
     */
    public String getType() {
        return this.type;
    }

    /**
     * getter for submitted date member variable
     *
     * @author Aditya Joshi
     * @return submitted date of project
     */
    public int getSubmitdate() {
        return submitdate;
    }
}
