package controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class HomeControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testFreelancer() {
        Result result = new HomeController().freelancer(61329129);
        Optional<String> actual = result.redirectLocation();
        Optional<String> expected = Optional.of("https://www.freelancer.com/projects/61329129/details");
        assertEquals(expected, actual);
    }

//    @Test
//    public void testIndex() {
//        Http.RequestBuilder request = new Http.RequestBuilder()
//                .method(GET)
//                .uri("/");
//
//        Result result = route(app, request);
//        assertEquals(OK, result.status());
//    }


//    @Test
//    public void testQueryFreelancer() throws ExecutionException, InterruptedException, IOException, TimeoutException {
//        Http.Request request = (Http.Request) new Http.RequestBuilder()
//                .method(POST)
//                .uri("/search")
//                .cookie(new Http.Cookie(
//                        "PLAY_SESSION",
//                        "eyJhbGciOiJIUzI1NiJ9.eyJkYXRhIjp7InNlc3Npb24iOiJNb3ppbGxhLzUuMCAoWDExOyBMaW51eCB4ODZfNjQpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS85OC4wLjQ3NTguODAgU2FmYXJpLzUzNy4zNiJ9LCJuYmYiOjE2NDc4MTg5NDksImlhdCI6MTY0NzgxODk0OX0.Fr6xE7wIz5hS1qP6ovA1_cl9brnFtAqfYFjO4djbsNY",
//                        600,
//                        "/",
//                        " ",
//                        false,
//                        true,
//                        Http.Cookie.SameSite.LAX
//                ));
//        CompletableFuture<Result> result = (CompletableFuture<Result>) new HomeController().queryFreeLancer(request);
//
//        assertEquals(OK, result.get().status());
//    }
//
//    @Test
//    public void testUserInfo() throws ExecutionException, InterruptedException, IOException, TimeoutException {
//        CompletableFuture<Result> result = (CompletableFuture<Result>) new HomeController().userInfo("8734788");
//
//        assertEquals(OK, result.get().status());
//    }
//
//    @Test
//    public void testGetSkills() throws ExecutionException, InterruptedException, IOException, TimeoutException {
//        CompletableFuture<Result> result = (CompletableFuture<Result>) new HomeController().getSkills("VB.NET");
//
//        assertEquals(OK, result.get().status());
//    }

//    @Test
//    public void testWordStats() throws ExecutionException, InterruptedException, IOException, TimeoutException {
//        CompletableFuture<Result> result = (CompletableFuture<Result>) new HomeController().wordStats("test");
//
//        assertEquals(OK, result.get().status());
//    }

//    @Test
//    public void testWordStatsIndividual() throws ExecutionException, InterruptedException, IOException, TimeoutException {
//        CompletableFuture<Result> result = (CompletableFuture<Result>) new HomeController().wordStatsIndividual("Create and Android application that can allow people to get bicycles on rent on an hourly basis. You");
//
//        assertEquals(OK, result.get().status());
//    }
}
