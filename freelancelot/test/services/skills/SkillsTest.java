package services.skills;

import actors.services.skills.SkillsActor;
import api.ApiConnector;
import models.Project;
import org.junit.Test;

import javax.net.ssl.SSLSession;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author Harsheen Kaur
 *
 * Class for unit test for feature skills
 */

public class SkillsTest {

    /**
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testGetProjectsList() throws ExecutionException, InterruptedException, TimeoutException, UnsupportedEncodingException {
        ApiConnector connection = mock(ApiConnector.class);

        HttpResponse<String> mockResp = new HttpResponse<String>() {

            /**
             *
             * @return
             */
            @Override
            public int statusCode() {
                return 200;
            }

            /**
             *
             * @return
             */
            @Override
            public HttpRequest request() {
                return null;
            }

            /**
             *
             * @return
             */
            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            /**
             *
             * @return
             */
            @Override
            public HttpHeaders headers() {
                return null;
            }

            /**
             *
             * @return
             */
            @Override
            public String body() {
                return "{\"status\":\"success\",\"result\":{\"projects\":[{\"id\":33255352,\"owner_id\":15671333,\"title\":\"VB.Net or  JAVA work( FROM PAKISTAN) needed related to CPU and RAM, network resources measurement\",\"status\":\"active\",\"seo_url\":\"java/Net-JAVA-work-FROM-PAKISTAN-33255352\",\"currency\":{\"id\":1,\"code\":\"USD\",\"sign\":\"$\",\"name\":\"US Dollar\",\"exchange_rate\":1.0,\"country\":\"US\",\"is_external\":false,\"is_escrowcom_supported\":true},\"description\":\"We need a VB.Net or  JAVA Freelancer from Pakistan related to measure the resources of RAM and CPU.\\nIt is easy work . we need a serious and hard working person from Pakistan\",\"jobs\":[{\"id\":7,\"name\":\"Java\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"java\",\"local\":false},{\"id\":14,\"name\":\"Visual Basic\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"visual_basic\",\"local\":false},{\"id\":15,\"name\":\".NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"dot_net\",\"local\":false},{\"id\":116,\"name\":\"Software Architecture\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"software_architecture\",\"local\":false},{\"id\":700,\"name\":\"VB.NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"vb_net\",\"local\":false}],\"submitdate\":1647785485,\"preview_description\":\"We need a VB.Net or  JAVA Freelancer from Pakistan related to measure the resources of RAM and CPU.\\n\",\"deleted\":false,\"nonpublic\":false,\"hidebids\":false,\"type\":\"hourly\",\"bidperiod\":7,\"budget\":{\"minimum\":2.0,\"maximum\":8.0},\"hourly_project_info\":{\"commitment\":{\"hours\":40,\"interval\":\"week\"},\"duration_enum\":\"unspecified\"},\"featured\":false,\"urgent\":false,\"bid_stats\":{\"bid_count\":2,\"bid_avg\":5.0},\"time_submitted\":1647785485,\"time_updated\":1647785485,\"upgrades\":{\"featured\":false,\"sealed\":false,\"nonpublic\":false,\"fulltime\":false,\"urgent\":false,\"qualified\":false,\"NDA\":false,\"ip_contract\":false,\"non_compete\":false,\"project_management\":false,\"pf_only\":false},\"language\":\"en\",\"hireme\":false,\"frontend_project_status\":\"open\",\"location\":{\"country\":{}},\"local\":false,\"negotiated\":false,\"time_free_bids_expire\":1647791475,\"pool_ids\":[\"freelancer\"],\"enterprise_ids\":[],\"is_escrow_project\":false,\"is_seller_kyc_required\":false,\"is_buyer_kyc_required\":false,\"project_reject_reason\":{}},{\"id\":33248153,\"owner_id\":15671333,\"title\":\"VB.Net or  JAVA work( FROM PAKISTAN) needed related to CPU and RAM\",\"status\":\"active\",\"seo_url\":\"java/Net-JAVA-work-FROM-PAKISTAN-33248153\",\"currency\":{\"id\":1,\"code\":\"USD\",\"sign\":\"$\",\"name\":\"US Dollar\",\"exchange_rate\":1.0,\"country\":\"US\",\"is_external\":false,\"is_escrowcom_supported\":true},\"description\":\"We need a VB.Net or  JAVA Freelancer from Pakistan related to measure the resources of RAM and CPU.\\nIt is easy work . we need a serious and hard working person from Pakistan\",\"jobs\":[{\"id\":7,\"name\":\"Java\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"java\",\"local\":false},{\"id\":14,\"name\":\"Visual Basic\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"visual_basic\",\"local\":false},{\"id\":15,\"name\":\".NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"dot_net\",\"local\":false},{\"id\":116,\"name\":\"Software Architecture\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"software_architecture\",\"local\":false},{\"id\":700,\"name\":\"VB.NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"vb_net\",\"local\":false}],\"submitdate\":1647705252,\"preview_description\":\"We need a VB.Net or  JAVA Freelancer from Pakistan related to measure the resources of RAM and CPU.\\n\",\"deleted\":false,\"nonpublic\":false,\"hidebids\":false,\"type\":\"hourly\",\"bidperiod\":7,\"budget\":{\"minimum\":2.0,\"maximum\":8.0},\"hourly_project_info\":{\"commitment\":{\"hours\":40,\"interval\":\"week\"},\"duration_enum\":\"unspecified\"},\"featured\":false,\"urgent\":false,\"bid_stats\":{\"bid_count\":6,\"bid_avg\":5.833333333333333},\"time_submitted\":1647705252,\"time_updated\":1647705252,\"upgrades\":{\"featured\":false,\"sealed\":false,\"nonpublic\":false,\"fulltime\":false,\"urgent\":false,\"qualified\":false,\"NDA\":false,\"ip_contract\":false,\"non_compete\":false,\"project_management\":false,\"pf_only\":false},\"language\":\"en\",\"hireme\":false,\"frontend_project_status\":\"open\",\"location\":{\"country\":{}},\"local\":false,\"negotiated\":false,\"time_free_bids_expire\":1647711242,\"pool_ids\":[\"freelancer\"],\"enterprise_ids\":[],\"is_escrow_project\":false,\"is_seller_kyc_required\":false,\"is_buyer_kyc_required\":false,\"project_reject_reason\":{}},{\"id\":33240608,\"owner_id\":15671333,\"title\":\"VB.Net or  JAVA work( FROM PAKISTAN) needed related to measure the resources of RAM and CPU\",\"status\":\"active\",\"seo_url\":\"java/Net-JAVA-work-FROM-PAKISTAN\",\"currency\":{\"id\":1,\"code\":\"USD\",\"sign\":\"$\",\"name\":\"US Dollar\",\"exchange_rate\":1.0,\"country\":\"US\",\"is_external\":false,\"is_escrowcom_supported\":true},\"description\":\"We need a VB.Net or  JAVA Freelancer from Pakistan related to measure the resources of RAM and CPU.\\nI need work related to network resources\",\"jobs\":[{\"id\":7,\"name\":\"Java\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"java\",\"local\":false},{\"id\":14,\"name\":\"Visual Basic\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"visual_basic\",\"local\":false},{\"id\":15,\"name\":\".NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"dot_net\",\"local\":false},{\"id\":116,\"name\":\"Software Architecture\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"software_architecture\",\"local\":false},{\"id\":700,\"name\":\"VB.NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"vb_net\",\"local\":false}],\"submitdate\":1647626318,\"preview_description\":\"We need a VB.Net or  JAVA Freelancer from Pakistan related to measure the resources of RAM and CPU.\\n\",\"deleted\":false,\"nonpublic\":false,\"hidebids\":false,\"type\":\"hourly\",\"bidperiod\":7,\"budget\":{\"minimum\":2.0,\"maximum\":8.0},\"hourly_project_info\":{\"commitment\":{\"hours\":40,\"interval\":\"week\"},\"duration_enum\":\"unspecified\"},\"featured\":false,\"urgent\":false,\"bid_stats\":{\"bid_count\":0},\"time_submitted\":1647626318,\"time_updated\":1647626318,\"upgrades\":{\"featured\":false,\"sealed\":false,\"nonpublic\":false,\"fulltime\":false,\"urgent\":false,\"qualified\":false,\"NDA\":false,\"ip_contract\":false,\"non_compete\":false,\"project_management\":false,\"pf_only\":false},\"language\":\"en\",\"hireme\":false,\"frontend_project_status\":\"open\",\"location\":{\"country\":{}},\"local\":false,\"negotiated\":false,\"time_free_bids_expire\":1647632275,\"pool_ids\":[\"freelancer\"],\"enterprise_ids\":[],\"is_escrow_project\":false,\"is_seller_kyc_required\":false,\"is_buyer_kyc_required\":false,\"project_reject_reason\":{}},{\"id\":33239799,\"owner_id\":15671333,\"title\":\"VB.Net or  JAVA work needed related to measure the resources of RAM and CPU\",\"status\":\"active\",\"seo_url\":\"vb-net/Net-JAVA-work-needed-related\",\"currency\":{\"id\":1,\"code\":\"USD\",\"sign\":\"$\",\"name\":\"US Dollar\",\"exchange_rate\":1.0,\"country\":\"US\",\"is_external\":false,\"is_escrowcom_supported\":true},\"description\":\"We need a VB.Net or  JAVA Freelancer related to measure the resources of RAM and CPU.\\nI need work related to network resources\",\"jobs\":[{\"id\":7,\"name\":\"Java\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"java\",\"local\":false},{\"id\":14,\"name\":\"Visual Basic\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"visual_basic\",\"local\":false},{\"id\":15,\"name\":\".NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"dot_net\",\"local\":false},{\"id\":116,\"name\":\"Software Architecture\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"software_architecture\",\"local\":false},{\"id\":700,\"name\":\"VB.NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"vb_net\",\"local\":false}],\"submitdate\":1647619391,\"preview_description\":\"We need a VB.Net or  JAVA Freelancer related to measure the resources of RAM and CPU.\\nI need work re\",\"deleted\":false,\"nonpublic\":false,\"hidebids\":false,\"type\":\"hourly\",\"bidperiod\":7,\"budget\":{\"minimum\":2.0,\"maximum\":8.0},\"hourly_project_info\":{\"commitment\":{\"hours\":40,\"interval\":\"week\"},\"duration_enum\":\"unspecified\"},\"featured\":false,\"urgent\":false,\"bid_stats\":{\"bid_count\":1,\"bid_avg\":5.0},\"time_submitted\":1647619391,\"time_updated\":1647619391,\"upgrades\":{\"featured\":false,\"sealed\":false,\"nonpublic\":false,\"fulltime\":false,\"urgent\":false,\"qualified\":false,\"NDA\":false,\"ip_contract\":false,\"non_compete\":false,\"project_management\":false,\"pf_only\":false},\"language\":\"en\",\"hireme\":false,\"frontend_project_status\":\"open\",\"location\":{\"country\":{}},\"local\":false,\"negotiated\":false,\"time_free_bids_expire\":1647625322,\"pool_ids\":[\"freelancer\"],\"enterprise_ids\":[],\"is_escrow_project\":false,\"is_seller_kyc_required\":false,\"is_buyer_kyc_required\":false,\"project_reject_reason\":{}},{\"id\":33237439,\"owner_id\":61293511,\"title\":\".net project developer\",\"status\":\"active\",\"seo_url\":\"vb-net/net-project-developer\",\"currency\":{\"id\":11,\"code\":\"INR\",\"sign\":\"\\u20b9\",\"name\":\"Indian Rupee\",\"exchange_rate\":0.013167,\"country\":\"IN\",\"is_external\":false,\"is_escrowcom_supported\":false},\"description\":\"To take part in software development and maintenance activities\\nProficiency in VB.net / Oracle DB\\nFinance industry experience is an added advantage.\",\"jobs\":[{\"id\":68,\"name\":\"SQL\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"sql\",\"local\":false},{\"id\":118,\"name\":\"Oracle\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"oracle\",\"local\":false},{\"id\":472,\"name\":\"Database Administration\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"database_administration\",\"local\":false},{\"id\":690,\"name\":\"ASP.NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"asp_net\",\"local\":false},{\"id\":700,\"name\":\"VB.NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"vb_net\",\"local\":false}],\"submitdate\":1647597587,\"preview_description\":\"To take part in software development and maintenance activities\\nProficiency in VB.net / Oracle DB\\nFi\",\"deleted\":false,\"nonpublic\":false,\"hidebids\":false,\"type\":\"hourly\",\"bidperiod\":7,\"budget\":{\"minimum\":750.0,\"maximum\":1250.0},\"hourly_project_info\":{\"commitment\":{\"hours\":40,\"interval\":\"week\"},\"duration_enum\":\"unspecified\"},\"featured\":false,\"urgent\":false,\"bid_stats\":{\"bid_count\":8,\"bid_avg\":1097.5},\"time_submitted\":1647597587,\"time_updated\":1647597587,\"upgrades\":{\"featured\":false,\"sealed\":false,\"nonpublic\":false,\"fulltime\":false,\"urgent\":false,\"qualified\":false,\"NDA\":false,\"ip_contract\":false,\"non_compete\":false,\"project_management\":false,\"pf_only\":false},\"language\":\"en\",\"hireme\":false,\"frontend_project_status\":\"open\",\"location\":{\"country\":{}},\"local\":false,\"negotiated\":false,\"time_free_bids_expire\":1647593805,\"pool_ids\":[\"freelancer\"],\"enterprise_ids\":[],\"is_escrow_project\":false,\"is_seller_kyc_required\":false,\"is_buyer_kyc_required\":false,\"project_reject_reason\":{}},{\"id\":33222581,\"owner_id\":61268612,\"title\":\"Blockchain Project\",\"status\":\"active\",\"seo_url\":\"blockchain/Blockchain-Project-33222581\",\"currency\":{\"id\":1,\"code\":\"USD\",\"sign\":\"$\",\"name\":\"US Dollar\",\"exchange_rate\":1.0,\"country\":\"US\",\"is_external\":false,\"is_escrowcom_supported\":true},\"description\":\"Hi,\\nI have a blockchain full-stack project which requires some maintenance. There are some functions that have been deprecated, with the latest update in metamask some of the windows methods are no longer supported which are used in my project. I need someone to update the code and make it work. There are some additional functionalities as well. \\n\\nTo help with the task I have the complete code which was working before the update and also a step-by-step guide on using the code.\\n\\nThis project uses - web3.js, javascript, SQL Server, VB.net\\n\\nFurthermore, I do have a deadline for this project so ping me if you are currently available.\\n\\nThank You.\",\"jobs\":[{\"id\":989,\"name\":\"Blockchain\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"blockchain\",\"local\":false},{\"id\":1088,\"name\":\"Full Stack Development\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"full_stack_development\",\"local\":false},{\"id\":2377,\"name\":\"Web3.js\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"web3js\",\"local\":false}],\"submitdate\":1647452973,\"preview_description\":\"Hi,\\nI have a blockchain full-stack project which requires some maintenance. There are some functions\",\"deleted\":false,\"nonpublic\":false,\"hidebids\":false,\"type\":\"hourly\",\"bidperiod\":7,\"budget\":{\"minimum\":15.0,\"maximum\":25.0},\"hourly_project_info\":{\"commitment\":{\"hours\":40,\"interval\":\"week\"},\"duration_enum\":\"unspecified\"},\"featured\":false,\"urgent\":false,\"bid_stats\":{\"bid_count\":60,\"bid_avg\":21.3},\"time_submitted\":1647452973,\"time_updated\":1647452973,\"upgrades\":{\"featured\":false,\"sealed\":false,\"nonpublic\":false,\"fulltime\":false,\"urgent\":false,\"qualified\":false,\"NDA\":false,\"ip_contract\":false,\"non_compete\":false,\"project_management\":false,\"pf_only\":false},\"language\":\"en\",\"hireme\":false,\"frontend_project_status\":\"open\",\"location\":{\"country\":{}},\"local\":false,\"negotiated\":false,\"time_free_bids_expire\":1647447957,\"pool_ids\":[\"freelancer\"],\"enterprise_ids\":[],\"is_escrow_project\":false,\"is_seller_kyc_required\":false,\"is_buyer_kyc_required\":false,\"project_reject_reason\":{}},{\"id\":33218276,\"owner_id\":39729378,\"title\":\"Coding on VB.net / Oracle DB / Open source / and other technical requirements to ensure updates and progression of ERP software\",\"status\":\"active\",\"seo_url\":\"coding/Coding-net-Oracle-Open-source\",\"currency\":{\"id\":11,\"code\":\"INR\",\"sign\":\"\\u20b9\",\"name\":\"Indian Rupee\",\"exchange_rate\":0.013167,\"country\":\"IN\",\"is_external\":false,\"is_escrowcom_supported\":false},\"description\":\"Responsibilities\\n1.    To review the current ERP system in the company.\\n2     To understand the existing implementations and to decide to refactor or \\n       rewrite the codebase.\\n1.\\tTo work closely with staff of various departments to analyse the system needs and provide solution then and there. \\n2.\\tTo produce detailed specifications and write the program codes.\\n3.\\tTo identify Performance bottlenecks and use good coding patterns to avoid them in the first place.\\n4.\\tTo test the product in controlled, real situations before going live.\\n5.\\tTo support the automation or ERP module once it is live, in case of bugs / errors / issues.\\n6.\\tTo maintain the systems once they are up and running. \\n7.\\tResponsible for error free and fool proof ERP implementation in the organization\\n8.\\tTo develop a strong database with all the financial and non-financial data, available for ready reference in ERP.\\n9.\\tTo work closely with employees of various departments to analyse the system needs. \\n10.\\tTo perform periodical system monitoring and analyse the operational needs. \\n11.\\tTo present ideas for system improvements, including cost proposals, man days required, project deliverable deadline, etc.\\n12.\\tTo develop and maintain installation and configuration procedures.\\n13.\\tTo manage hardware and software upgrades and patches.\\n14.\\tTo troubleshoot software/hardware problems reported by users (employees).\\n15.\\tTo provide all IT related facilities and accesses to employees. \\n16.\\tTo research and recommend innovative, approaches for automating system administration tasks.\\n\",\"jobs\":[{\"id\":116,\"name\":\"Software Architecture\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"software_architecture\",\"local\":false},{\"id\":613,\"name\":\"Software Development\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"software_development\",\"local\":false},{\"id\":954,\"name\":\"Coding\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"coding\",\"local\":false},{\"id\":962,\"name\":\"Programming\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"programming\",\"local\":false}],\"submitdate\":1647417899,\"preview_description\":\"Responsibilities\\n1.    To review the current ERP system in the company.\\n2     To understand the exis\",\"deleted\":false,\"nonpublic\":false,\"hidebids\":false,\"type\":\"fixed\",\"bidperiod\":7,\"budget\":{\"minimum\":12500.0,\"maximum\":37500.0},\"featured\":false,\"urgent\":false,\"bid_stats\":{\"bid_count\":5,\"bid_avg\":31900.0},\"time_submitted\":1647417899,\"time_updated\":1647417899,\"upgrades\":{\"featured\":false,\"sealed\":false,\"nonpublic\":false,\"fulltime\":false,\"urgent\":false,\"qualified\":false,\"NDA\":false,\"ip_contract\":false,\"non_compete\":false,\"project_management\":false,\"pf_only\":false},\"language\":\"en\",\"hireme\":false,\"frontend_project_status\":\"open\",\"location\":{\"country\":{}},\"local\":false,\"negotiated\":false,\"time_free_bids_expire\":1647414267,\"pool_ids\":[\"freelancer\"],\"enterprise_ids\":[],\"is_escrow_project\":false,\"is_seller_kyc_required\":false,\"is_buyer_kyc_required\":false,\"project_reject_reason\":{}},{\"id\":33213646,\"owner_id\":61252464,\"title\":\"Healthcare project\",\"status\":\"active\",\"seo_url\":\"c-sharp-programming/Healthcare-project-33213646\",\"currency\":{\"id\":11,\"code\":\"INR\",\"sign\":\"\\u20b9\",\"name\":\"Indian Rupee\",\"exchange_rate\":0.013167,\"country\":\"IN\",\"is_external\":false,\"is_escrowcom_supported\":false},\"description\":\"This project is developed by using windows platform, c#, vb.net and SQL server stored procedures. \\nI have items need to complete in stored procedures logic\",\"jobs\":[{\"id\":15,\"name\":\".NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"dot_net\",\"local\":false},{\"id\":68,\"name\":\"SQL\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"sql\",\"local\":false},{\"id\":106,\"name\":\"C# Programming\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"c_sharp_programming\",\"local\":false},{\"id\":320,\"name\":\"C++ Programming\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"cplusplus_programming\",\"local\":false},{\"id\":695,\"name\":\"Microsoft SQL Server\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"microsoft_sql_server\",\"local\":false}],\"submitdate\":1647371814,\"preview_description\":\"This project is developed by using windows platform, c#, vb.net and SQL server stored procedures. \\nI\",\"deleted\":false,\"nonpublic\":false,\"hidebids\":false,\"type\":\"hourly\",\"bidperiod\":7,\"budget\":{\"minimum\":750.0,\"maximum\":1250.0},\"hourly_project_info\":{\"commitment\":{\"hours\":40,\"interval\":\"week\"},\"duration_enum\":\"unspecified\"},\"featured\":false,\"urgent\":false,\"bid_stats\":{\"bid_count\":21,\"bid_avg\":1107.142857142857},\"time_submitted\":1647371814,\"time_updated\":1647371814,\"upgrades\":{\"featured\":false,\"sealed\":false,\"nonpublic\":false,\"fulltime\":false,\"urgent\":false,\"qualified\":false,\"NDA\":false,\"ip_contract\":false,\"non_compete\":false,\"project_management\":false,\"pf_only\":false},\"language\":\"en\",\"hireme\":false,\"frontend_project_status\":\"open\",\"location\":{\"country\":{}},\"local\":false,\"negotiated\":false,\"time_free_bids_expire\":1647367473,\"pool_ids\":[\"freelancer\"],\"enterprise_ids\":[],\"is_escrow_project\":false,\"is_seller_kyc_required\":false,\"is_buyer_kyc_required\":false,\"project_reject_reason\":{}},{\"id\":33201675,\"owner_id\":51978257,\"title\":\"VITROS 3600 Lab Equipment Interface using TCP/IP port no (ASTM protocol)\",\"status\":\"active\",\"seo_url\":\"vb-net/VITROS-Lab-Equipment-Interface-using\",\"currency\":{\"id\":11,\"code\":\"INR\",\"sign\":\"\\u20b9\",\"name\":\"Indian Rupee\",\"exchange_rate\":0.013167,\"country\":\"IN\",\"is_external\":false,\"is_escrowcom_supported\":false},\"description\":\"Robust and reliable application using vb.net code using windows forms, \\nwith continuous listening TCP/IP port only (without ip address) without interruption,\\n if disconnected send reconnect request again to the machine. \\nSending test Order against machine query and capturing received data, \\nsending appropriate ASTM response to the machine.\\n(asynchronous or synchronous listening with socket or tcpclient).\\n\\nThe main aim is to without disconnection with the machine.\",\"jobs\":[{\"id\":6,\"name\":\"C Programming\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"c_programming\",\"local\":false},{\"id\":15,\"name\":\".NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"dot_net\",\"local\":false},{\"id\":106,\"name\":\"C# Programming\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"c_sharp_programming\",\"local\":false},{\"id\":116,\"name\":\"Software Architecture\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"software_architecture\",\"local\":false},{\"id\":700,\"name\":\"VB.NET\",\"category\":{\"id\":1,\"name\":\"Websites, IT & Software\"},\"seo_url\":\"vb_net\",\"local\":false}],\"submitdate\":1647271630,\"preview_description\":\"Robust and reliable application using vb.net code using windows forms, \\nwith continuous listening TC\",\"deleted\":false,\"nonpublic\":false,\"hidebids\":false,\"type\":\"fixed\",\"bidperiod\":7,\"budget\":{\"minimum\":12500.0,\"maximum\":37500.0},\"featured\":false,\"urgent\":false,\"bid_stats\":{\"bid_count\":3,\"bid_avg\":22500.0},\"time_submitted\":1647271630,\"time_updated\":1647271630,\"upgrades\":{\"featured\":false,\"sealed\":false,\"nonpublic\":false,\"fulltime\":false,\"urgent\":false,\"qualified\":false,\"NDA\":false,\"ip_contract\":false,\"non_compete\":false,\"project_management\":false,\"pf_only\":false},\"language\":\"en\",\"hireme\":false,\"frontend_project_status\":\"open\",\"location\":{\"country\":{}},\"local\":false,\"negotiated\":false,\"time_free_bids_expire\":1647268030,\"pool_ids\":[\"freelancer\"],\"enterprise_ids\":[],\"is_escrow_project\":false,\"is_seller_kyc_required\":false,\"is_buyer_kyc_required\":false,\"project_reject_reason\":{}}],\"total_count\":9},\"request_id\":\"c8333759c23cb6eb26b5e320a5406ef3\"}";
            }

            /**
             *
             * @return
             */
            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            /**
             *
             * @return
             */
            @Override
            public URI uri() {
                return null;
            }

            /**
             *
             * @return
             */
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
                (CompletableFuture<List<Project>>) Skills.getProjectsList(
                        connection,
                        "VB.NET"
                );
        assertNotNull(resultInFuture.get());
        assertEquals(resultInFuture.get().size(), 6);
        verify(connection, times(1)).sendRequest();
    }
}
