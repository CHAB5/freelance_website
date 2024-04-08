package services.employerprofile;

import api.ApiConnector;
import models.Project;
import wrapper.UserDetailsResponse;
import org.junit.Test;
import play.mvc.Result;
import static play.mvc.Http.Status.OK;

import javax.net.ssl.SSLSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Test class for EmployerInfoHelper class
 *
 * @author Aditya Joshi
 */
public class EmployerInfoHelperTest {
    /**
     * tests getEmployerProfilePageDetails() method that is being called from home controller
     *
     * @author Aditya Joshi
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws IOException
     */
    @Test
    public void testGetEmployerProfilePageDetails() throws ExecutionException, InterruptedException, TimeoutException, IOException {
        ApiConnector connection = mock(ApiConnector.class);

        HttpResponse<String> mockResp = new HttpResponse<String>() {
            @Override
            public int statusCode() {
                return 200;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
                return "{'request_id':'123123123','result':{'id':61275785, 'username':'testusername', 'projects':[{'id':12345678, 'submitdate': 1642585594}]}}";
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public HttpClient.Version version() {
                return null;
            }
        };

        CompletableFuture<HttpResponse<String>> futureResp
                = CompletableFuture.supplyAsync(
                () -> {
//                    System.out.println("connection.getEndpointURL() in TEST ENV  " + connection.getEndpointURL());
                    return mockResp;
                }
        );
        when(connection.sendRequest()).thenReturn(
                futureResp
        );
//        System.out.println("***** here *****");
        CompletableFuture<Result> resultInFuture =
                (CompletableFuture<Result>) EmployerInfoHelper.getEmployerProfilePageDetails(
                        connection,
                        "48465937"
                );
        assertNotNull(resultInFuture.get());
        verify(connection, times(2)).sendRequest();
    }

    /**
     * tests employer details callout
     *
     * @author Aditya Joshi
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    @Test
    public void testGetEmployerDetails() throws ExecutionException, InterruptedException, TimeoutException {
        ApiConnector connection = mock(ApiConnector.class);

        HttpResponse<String> mockRespForEmployerDetails = new HttpResponse<String>() {
            @Override
            public int statusCode() {
                return 200;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
//                return "{'request_id':'123123123','result':{'id':61275785, 'username':'testusername', 'projects':[{'id':'test123'}]}}";
                return "{'status':'success','result':{'id':58953585,'username':'PariP123','suspended':null,'closed':false,'is_active':null,'force_verify':null,'avatar':'/img/unknown.png','email':null,'reputation':{'entire_history':{'overall':0.0,'on_budget':0.0,'on_time':0.0,'positive':0.0,'category_ratings':{'quality':0.0,'communication':0.0,'expertise':0.0,'professionalism':0.0,'hire_again':0.0},'all':0,'reviews':0,'incomplete_reviews':0,'complete':0,'incomplete':0,'earnings':null,'completion_rate':0.0,'rehire_rate':null,'user_id':58953585,'completed_relevant_job_count':null},'last3months':{'overall':0.0,'on_budget':0.0,'on_time':0.0,'positive':0.0,'category_ratings':{'quality':0.0,'communication':0.0,'expertise':0.0,'professionalism':0.0,'hire_again':0.0},'all':0,'reviews':0,'incomplete_reviews':0,'complete':0,'incomplete':0,'earnings':null,'completion_rate':0.0,'rehire_rate':null,'user_id':58953585,'completed_relevant_job_count':null},'last12months':{'overall':0.0,'on_budget':0.0,'on_time':0.0,'positive':0.0,'category_ratings':{'quality':0.0,'communication':0.0,'expertise':0.0,'professionalism':0.0,'hire_again':0.0},'all':0,'reviews':0,'incomplete_reviews':0,'complete':0,'incomplete':0,'earnings':null,'completion_rate':0.0,'rehire_rate':null,'user_id':58953585,'completed_relevant_job_count':null},'user_id':58953585,'role':'freelancer','earnings_score':0.0,'job_history':{'count_other':0,'job_counts':[]},'project_stats':null},'jobs':[],'profile_description':null,'hourly_rate':null,'registration_date':1636199103,'limited_account':false,'display_name':'PariP123','tagline':null,'location':{'country':{'name':'Germany','flag_url':'/img/flags/png/de.png','code':'de','highres_flag_url':'/img/flags/highres_png/germany.png','flag_url_cdn':'//cdn6.f-cdn.com/img/flags/png/de.png','highres_flag_url_cdn':'//cdn5.f-cdn.com/img/flags/highres_png/germany.png','iso3':null,'region_id':null,'phone_code':null,'demonym':null,'person':null,'seo_url':null,'sanction':null,'language_code':null,'language_id':null},'city':'Kaarst','latitude':49.873,'longitude':8.651,'vicinity':'Darmstadt','administrative_area':'Hessen','full_address':null,'administrative_area_code':null,'postal_code':null},'avatar_large':'/img/unknown.png','role':'employer','chosen_role':'employer','employer_reputation':{'entire_history':{'overall':0.0,'on_budget':null,'on_time':null,'positive':0.0,'category_ratings':{'clarity_spec':0.0,'communication':0.0,'payment_prom':0.0,'professionalism':0.0,'work_for_again':0.0},'all':null,'reviews':null,'incomplete_reviews':null,'complete':null,'incomplete':null,'earnings':null,'completion_rate':null,'rehire_rate':null,'user_id':58953585,'completed_relevant_job_count':null},'last3months':{'overall':0.0,'on_budget':null,'on_time':null,'positive':0.0,'category_ratings':{'clarity_spec':0.0,'communication':0.0,'payment_prom':0.0,'professionalism':0.0,'work_for_again':0.0},'all':null,'reviews':null,'incomplete_reviews':null,'complete':null,'incomplete':null,'earnings':null,'completion_rate':null,'rehire_rate':null,'user_id':58953585,'completed_relevant_job_count':null},'last12months':{'overall':0.0,'on_budget':null,'on_time':null,'positive':0.0,'category_ratings':{'clarity_spec':0.0,'communication':0.0,'payment_prom':0.0,'professionalism':0.0,'work_for_again':0.0},'all':null,'reviews':null,'incomplete_reviews':null,'complete':null,'incomplete':null,'earnings':null,'completion_rate':null,'rehire_rate':null,'user_id':58953585,'completed_relevant_job_count':null},'user_id':58953585,'role':'employer','earnings_score':0.0,'job_history':{'count_other':0,'job_counts':[]},'project_stats':{'open':1,'work_in_progress':0,'complete':0,'pending':0,'draft':0}},'status':{'payment_verified':false,'email_verified':true,'deposit_made':false,'profile_complete':false,'phone_verified':false,'identity_verified':true,'facebook_connected':false,'freelancer_verified_user':false,'linkedin_connected':false},'avatar_cdn':'//cdn6.f-cdn.com/img/unknown.png','avatar_large_cdn':'//cdn6.f-cdn.com/img/unknown.png','primary_currency':{'id':8,'code':'EUR','sign':'\\u20ac','name':'Euro','exchange_rate':1.106971,'country':'EU','is_external':null,'is_escrowcom_supported':null},'account_balances':null,'membership_package':null,'qualifications':null,'badges':null,'primary_language':'en','search_languages':null,'cover_image':{'current_image':{'id':null,'url':'//cdn2.f-cdn.com/static/img/profiles/cover-product.jpg','description':'','width':1920,'height':550,'context':{'id':58953585,'type':'user_profile'}},'past_images':null},'true_location':null,'portfolio_count':null,'preferred_freelancer':false,'support_status':null,'first_name':null,'last_name':null,'mobile_tracking':null,'corporate':null,'public_name':'Parvin Sadat F.','corporate_users':null,'is_local':null,'address':null,'company':null,'recommendations':null,'pool_ids':[],'enterprise_ids':[],'date_of_birth':null,'escrowcom_account_linked':null,'escrowcom_interaction_required':false,'marketing_mobile_number':null,'user_sanctions':[],'freelancer_verified_status':null,'secure_phone':null,'directory_follow_preferences':null,'is_payment_confirmation_required':null,'is_nominated_payment_controlled':null,'is_nominated_payment_share_setup_complete':null,'spam_profile':null,'endorsements':null,'timezone':{'id':245,'country':'DE','timezone':'Europe/Berlin','offset':1.0},'responsiveness':null,'bid_quality_details':null,'test_user':null,'online_offline_status':null,'deposit_methods':null,'oauth_password_credentials_allowed':false,'registration_completed':true,'is_profile_visible':true,'enterprise_metadata_values':null,'mfa':null,'grants':null,'operating_areas':null,'equipment_groups':null},'request_id':'65de486d180722f69478e9ba47c635a2'}";
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public HttpClient.Version version() {
                return null;
            }
        };

        CompletableFuture<HttpResponse<String>> futureResp
                = CompletableFuture.supplyAsync(
                () -> {
//                    System.out.println("connection.getEndpointURL() in TEST ENV  " + connection.getEndpointURL());
                    return mockRespForEmployerDetails;
                }
        );
        when(connection.sendRequest()).thenReturn(
                futureResp
        );
//        System.out.println("***** here *****");
        CompletableFuture<UserDetailsResponse> resultInFuture =
                (CompletableFuture<UserDetailsResponse>) EmployerInfoHelper.getEmployerDetails(
                        connection,
                        "58953585"
                );
        assertEquals(resultInFuture.get().result.id, 58953585);
        verify(connection, times(1)).sendRequest();
    }

    /**
     * test for projects list api for a particular employer
     *
     * @author Aditya Joshi
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    @Test
    public void testGetProjectsList() throws ExecutionException, InterruptedException, TimeoutException {
        ApiConnector connection = mock(ApiConnector.class);

        HttpResponse<String> mockResp = new HttpResponse<String>() {
            @Override
            public int statusCode() {
                return 200;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
                return "{'status':'success','result':{'projects':[{'id':15663823,'owner_id':25832131,'title':'dsjlkdjsaldsaklk','status':'closed','sub_status':'closed_expired','seo_url':'website-design/dsjlkdjsaldsaklk','currency':{'id':3,'code':'AUD','sign':'$','name':'Australian Dollar','exchange_rate':0.740429,'country':'AU','is_external':false,'is_escrowcom_supported':true},'description':'fsafasfafasdfdfsdfdsfdsffgsdgafsdfadfsfsdfsd','submitdate':1574126129,'preview_description':'fsafasfafasdfdfsdfdsfdsffgsdgafsdfadfsfsdfsd','deleted':false,'nonpublic':false,'hidebids':false,'type':'fixed','bidperiod':7,'budget':{'minimum':250.0,'maximum':750.0},'featured':false,'urgent':false,'bid_stats':{'bid_count':0},'time_submitted':1574126129,'upgrades':{'featured':false,'sealed':false,'nonpublic':false,'fulltime':false,'urgent':false,'qualified':false,'NDA':false,'ip_contract':false,'non_compete':false,'project_management':false,'pf_only':false},'language':'en','hireme':false,'frontend_project_status':'complete','location':{'country':{}},'local':false,'negotiated':false,'time_free_bids_expire':1574119002,'pool_ids':['freelancer'],'enterprise_ids':[],'is_escrow_project':false,'is_seller_kyc_required':false,'is_buyer_kyc_required':false,'project_reject_reason':{}},{'id':15663824,'owner_id':25832131,'title':'asfhghkkhgjghjhgj','status':'closed','sub_status':'closed_awarded','seo_url':'logo-design/asfhghkkhgjghjhgj','currency':{'id':3,'code':'AUD','sign':'$','name':'Australian Dollar','exchange_rate':0.740429,'country':'AU','is_external':false,'is_escrowcom_supported':true},'description':'hjdhgfhgkhgfjythgdfgdfgdfgfgfdgafsasdassafassfasfadsadsadsa','submitdate':1574126098,'preview_description':'hjdhgfhgkhgfjythgdfgdfgdfgfgfdgafsasdassafassfasfadsadsadsa','deleted':false,'nonpublic':false,'hidebids':false,'type':'fixed','bidperiod':14,'budget':{'minimum':250.0,'maximum':750.0},'featured':false,'urgent':false,'bid_stats':{'bid_count':1,'bid_avg':555.0},'time_submitted':1574126098,'upgrades':{'featured':false,'sealed':false,'nonpublic':false,'fulltime':false,'urgent':false,'qualified':false,'NDA':false,'ip_contract':false,'non_compete':false,'project_management':false,'pf_only':false},'language':'en','hireme':false,'frontend_project_status':'complete','location':{'country':{}},'local':false,'negotiated':false,'time_free_bids_expire':1574119123,'pool_ids':['freelancer'],'enterprise_ids':[],'is_escrow_project':false,'is_seller_kyc_required':false,'is_buyer_kyc_required':false,'project_reject_reason':{}}]},'request_id':'18d0045afefda6feb2c5ee08156def9d'}";
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public HttpClient.Version version() {
                return null;
            }
        };

        CompletableFuture<HttpResponse<String>> futureResp
                = CompletableFuture.supplyAsync(
                () -> {
//                    System.out.println("connection.getEndpointURL() in TEST ENV  " + connection.getEndpointURL());
                    return mockResp;
                }
        );
        when(connection.sendRequest()).thenReturn(
                futureResp
        );
//        System.out.println("***** here *****");
        CompletableFuture<List<Project>> resultInFuture =
                (CompletableFuture<List<Project>>) EmployerInfoHelper.getProjectsList(
                        connection,
                        "48465937"
                );
        assertNotNull(resultInFuture.get());
        assertEquals(resultInFuture.get().size(), 2);
        verify(connection, times(1)).sendRequest();
    }

    /**
     * test class for a method that combine results of two callouts
     *
     * @author Aditya Joshi
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    @Test
    public void testGetResultData() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        CompletionStage<Result> result = EmployerInfoHelper.getResultData(
                CompletableFuture.supplyAsync(
                        () -> {
                            List<Project> lstProj = new ArrayList<>();
                            lstProj.add(new Project());
                            return lstProj;
                        }
                ),
                CompletableFuture.supplyAsync(
                        () -> {
                            UserDetailsResponse resp = new UserDetailsResponse();
                            resp.request_id = "test";
                            return resp;
                        }
                )
        );
        Result syncRes = result.toCompletableFuture().get();
        assertEquals(OK, syncRes.status());
    }

}