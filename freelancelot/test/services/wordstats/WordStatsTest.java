package services.wordstats;

import api.ApiConnector;
import models.Project;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import play.mvc.Result;

import javax.net.ssl.SSLSession;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static play.mvc.Http.Status.OK;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Test class for Word Stats services
 *
 * @author Chandana Basavaraj
 */
public class WordStatsTest {

    /**
     * Test case for wordFrequency method which takes list of projects as parameter
     *
     * @author Chandana Basavaraj
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void GlobalWordStatsTest() throws IOException, ExecutionException, InterruptedException {

        List<Project> list=new ArrayList<Project>();
        Project proj1 = new Project();
        proj1.preview_description = "Apple is a fruit";

        Project proj2 = new Project();
        proj2.preview_description = " Montreal is a city";

        list.add(proj1);
        list.add(proj2);

        Map<String, Integer> testMap = new HashMap<String, Integer>();

        testMap.put("a", 2);
        testMap.put("is",2);
        testMap.put("Apple", 1);
        testMap.put("city",1);
        testMap.put("fruit", 1);
        testMap.put("Montreal", 1);

        Assertions.assertEquals(
                WordStats.wordFrequency(list),
                testMap
        );
    }

    /**
     * Test case for wordFrequencyIndividual method which takes preview_description as parameter
     *
     * @author Chandana Basavaraj
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void IndividualWordStatsTest() throws IOException, ExecutionException, InterruptedException {
        Project proj1 = new Project();
        proj1.preview_description = "Apple is a fruit";

        Map<String, Integer> proj1testMap = new HashMap<String, Integer>();
        proj1testMap.put("a", 1);
        proj1testMap.put("Apple",1);
        proj1testMap.put("fruit",1);
        proj1testMap.put("is",1);

        Assertions.assertEquals(
                ((CompletableFuture<Result>) WordStats.wordFrequencyIndividual(proj1.preview_description)).get().status(),
                OK
        );
    }

    /**
     * Test case to check for null for wordFrequency method which takes list of projects as parameter
     *
     * @author Chandana Basavaraj
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void descriptionOfProjectEmptyTest() throws IOException, ExecutionException, InterruptedException {
        List<Project> list=new ArrayList<Project>();
        Project proj1 = new Project();
        Project proj2 = new Project();
        proj1.preview_description = "World ";
        proj2.preview_description = "Hello World";

        list.add(proj1);
        list.add(proj2);

        Map<String, Integer> testMap = new HashMap<String, Integer>();

        Assertions.assertNotNull(
                WordStats.wordFrequency(list)
        );
    }

    /**
     * Test case for getWordStatsPageDetails method
     * Testing with Mock object
     *
     * @author Chandana Basavaraj
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws IOException
     */
    @Test
    public void TestgetWordStatsPageDetails() throws ExecutionException, InterruptedException, TimeoutException, IOException {
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
        Assertions.assertEquals(
                ((CompletableFuture<Result>) WordStats.getWordStatsPageDetails(connection,"test")).get().status(),
                OK
        );
        verify(connection, times(1)).sendRequest();
    }
}