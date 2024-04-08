package wrapper;

import models.Project;
import java.util.ArrayList;

/**
 * ProjectListResponse is a POJO class representing response for Project List API
 *@author Aditya Joshi
 *
 */
public class ProjectsListResponse {

    public String status;
    public Result result;
    public String request_id;

    public class Result{
        public ArrayList<Project> projects;
        public Object users;
        public Object selected_bids;
        public int total_count;

    }

    public class Currency{
        public int id;
        public String code;
        public String sign;
        public String name;
        public double exchange_rate;
        public String country;
        public boolean is_external;
        public boolean is_escrowcom_supported;

    }

    public class Budget{
        public double minimum;
        public double maximum;
        public Object name;
        public Object project_type;
        public Object currency_id;

    }

    public class Commitment{
        public int hours;
        public String interval;

    }

    public class HourlyProjectInfo{
        public Commitment commitment;
        public String duration_enum;

    }

    public class BidStats{
        public int bid_count;
        public double bid_avg;

    }

    public class Upgrades{
        public boolean featured;
        public boolean sealed;
        public boolean nonpublic;
        public boolean fulltime;
        public boolean urgent;
        public boolean qualified;
        public boolean NDA;
        public Object assisted;
        public Object active_prepaid_milestone;
        public boolean ip_contract;
        public Object success_bundle;
        public boolean non_compete;
        public boolean project_management;
        public boolean pf_only;
        public Object recruiter;
        public Object listed;
        public Object extend;
        public Object unpaid_recruiter;

    }

    public class Country{
        public Object name;
        public Object flag_url;
        public Object code;
        public Object highres_flag_url;
        public Object flag_url_cdn;
        public Object highres_flag_url_cdn;
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
        public Object city;
        public Object latitude;
        public Object longitude;
        public Object vicinity;
        public Object administrative_area;
        public Object full_address;
        public Object administrative_area_code;
        public Object postal_code;

    }

    public class ProjectRejectReason{
        public Object description;
        public Object message;

    }


}
