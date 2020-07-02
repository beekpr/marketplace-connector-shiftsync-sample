package io.beekeeper.connector.shiftsync.sample;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class ExampleShiftRestService {
    public final static String API_TOKEN = "anApiToken";

    private final WireMockServer wireMock = new WireMockServer(
            options()
                .dynamicPort()
                .extensions(new ResponseTemplateTransformer(false))
                .withRootDirectory("src/test/resources/exampleShiftRestService")
    );

    public static void main(String[] args) {
        ExampleShiftRestService exampleService = new ExampleShiftRestService();
        exampleService.start();
        System.out.println("Example Shift REST Service is available at " + exampleService.getBaseUrl());
    }

    public ExampleShiftRestService() {
        stubExternalApi();
    }

    public void start() {
        wireMock.start();
    }

    public void stop() {
        wireMock.stop();
    }

    public String getBaseUrl() {
        return wireMock.baseUrl();
    }

    public String getApiToken() {
        return API_TOKEN;
    }

    private void stubExternalApi() {
        wireMock.stubFor(
            get("/api/1/schedules")
                .withHeader("Authorization", equalTo("Bearer " + API_TOKEN))
                .willReturn(
                    aResponse()
                        .withBodyFile("getSchedulesResponse.json")
                        .withTransformers("response-template")
                )
        );

        wireMock.stubFor(
            get(urlPathEqualTo("/api/1/shifts"))
                .withHeader("Authorization", equalTo("Bearer " + API_TOKEN))
                .withQueryParam("scheduleId", any())
                .willReturn(
                    aResponse()
                        .withBodyFile("getShiftsForScheduleResponse.json")
                        .withTransformers("response-template")
                )
        );
    }

    private static StringValuePattern any() {
        return containing("");
    }
}
