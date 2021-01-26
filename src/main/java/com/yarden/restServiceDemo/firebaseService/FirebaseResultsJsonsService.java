package com.yarden.restServiceDemo.firebaseService;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.yarden.restServiceDemo.Logger;
import com.yarden.restServiceDemo.pojos.EyesResultRequestJson;
import com.yarden.restServiceDemo.pojos.RequestInterface;
import com.yarden.restServiceDemo.pojos.SdkResultRequestJson;
import javassist.NotFoundException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

@Configuration
public class FirebaseResultsJsonsService extends TimerTask {
    private static AtomicReference<LinkedList<String>> sdkRequestQueue = new AtomicReference<>();
    private static AtomicReference<LinkedList<String>> eyesRequestQueue = new AtomicReference<>();
    private static boolean isRunning = false;
    private static Timer timer;

    @EventListener(ApplicationReadyEvent.class)
    public static synchronized void start() {
        if (!isRunning) {
            timer = new Timer("FirebaseQueue");
            if (sdkRequestQueue.get() == null) {
                sdkRequestQueue.set(new LinkedList<>());
            }
            if (eyesRequestQueue.get() == null) {
                eyesRequestQueue.set(new LinkedList<>());
            }
            timer.scheduleAtFixedRate(new FirebaseResultsJsonsService(), 30, 100);
            isRunning = true;
            Logger.info("FirebaseQueue started");
        }
    }

    @Override
    public synchronized void run() {
        if (!sdkRequestQueue.get().isEmpty()){
            SdkResultRequestJson sdkResultRequestJson = new Gson().fromJson(sdkRequestQueue.get().removeFirst(), SdkResultRequestJson.class);
            addRequestToFirebase(sdkResultRequestJson, FirebasePrefixStrings.Sdk);
            System.gc();
        }
        if (!eyesRequestQueue.get().isEmpty()){
            EyesResultRequestJson eyesResultRequestJson = new Gson().fromJson(eyesRequestQueue.get().removeFirst(), EyesResultRequestJson.class);
            addRequestToFirebase(eyesResultRequestJson, FirebasePrefixStrings.Eyes);
            System.gc();
        }
    }

    private static void addRequestToQueue(String json, AtomicReference<LinkedList<String>> requestQueue){
        try {
            requestQueue.get().add(json);
        } catch (NullPointerException e) {
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void addSdkRequestToFirebase(String json) {
        addRequestToQueue(json, sdkRequestQueue);
    }

    public static void addEyesRequestToFirebase(String json) {
        addRequestToQueue(json, eyesRequestQueue);
    }

    public static String getCurrentSdkRequestFromFirebase(String id, String group) throws NotFoundException {
        try {
            return getCurrentRequestFromFirebase(id, group, FirebasePrefixStrings.Sdk);
        } catch (Throwable t) {
            throw new NotFoundException("");
        }
    }

    public static String getCurrentEyesRequestFromFirebase(String id, String group) throws NotFoundException {
        try {
            return getCurrentRequestFromFirebase(id, group, FirebasePrefixStrings.Eyes);
        } catch (Throwable t) {
            throw new NotFoundException("");
        }
    }

    private static void addRequestToFirebase(RequestInterface request, FirebasePrefixStrings fileNamePrefixInFirebase) {
        if ((request.getSandbox() != null) && request.getSandbox()) {
            return;
        }
        RequestInterface resultRequestJsonFromFirebase = null;
        try {
            String currentRequestFromFirebase = getCurrentRequestFromFirebase(request.getId(), request.getGroup(), fileNamePrefixInFirebase);
            resultRequestJsonFromFirebase = new Gson().fromJson(currentRequestFromFirebase, request.getClass());
            JsonArray testResultsFromFirebase = resultRequestJsonFromFirebase.getResults();
            testResultsFromFirebase.addAll(request.getResults());
            resultRequestJsonFromFirebase.setResults(testResultsFromFirebase);
        } catch (Throwable t) {
            resultRequestJsonFromFirebase = request;
        }
        try {
            patchFirebaseRequest(request.getId(), request.getGroup(), new Gson().toJson(resultRequestJsonFromFirebase), fileNamePrefixInFirebase);
        } catch (IOException | InterruptedException e) {
            Logger.error("FirebaseResultsJsonsService: Failed to add result to firebase");
        }
    }

    private static String getResultRequestJsonFileName(String id, String group, String requestFileNamePrefix){
        return requestFileNamePrefix + "-" + id + "-" + group.toLowerCase();
    }

    private enum FirebasePrefixStrings {
        Sdk("Sdk"), Eyes("Eyes");

        public final String value;

        FirebasePrefixStrings(String value) {
            this.value = value;
        }

    }

    private static String getCurrentRequestFromFirebase(String id, String group, FirebasePrefixStrings fileNamePrefixInFirebase) throws IOException, InterruptedException, NotFoundException {
        String url = getFirebaseUrl(id, group, fileNamePrefixInFirebase);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String result = response.body();
        if (result.equals("null")) {
            throw new NotFoundException("");
        }
        return result;
    }

    private static void patchFirebaseRequest(String id, String group, String payload, FirebasePrefixStrings fileNamePrefixInFirebase) throws IOException, InterruptedException {
        String url = getFirebaseUrl(id, group, fileNamePrefixInFirebase);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(payload))
                .header("Content-Type", "application/json")
                .build();
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        HttpResponse response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
    }

    private static String getFirebaseUrl(String id, String group, FirebasePrefixStrings fileNamePrefixInFirebase){
        return "https://sdk-reports.firebaseio.com/" + getResultRequestJsonFileName(id, group, fileNamePrefixInFirebase.value) + ".json";
    }

}
