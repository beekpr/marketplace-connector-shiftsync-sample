package io.beekeeper.connector.shiftsync.sample.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.beekeeper.connector.shiftsync.sample.ShiftSyncException;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class SampleRestServiceClientTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options().dynamicPort());

    @Test
    public void shouldThrowAnExceptionForNotSuccessfulHttpCall() {
        // Given
        stubFor(get("/api/1/shifts?scheduleId=anInvalidId").willReturn(notFound()));
        final SampleRestServiceClient sampleRestServiceClient = SampleRestServiceClient.create(
            wireMockRule.baseUrl(),
            "anApiToken"
        );

        // When - Then
        assertThatExceptionOfType(ShiftSyncException.class)
            .isThrownBy(() -> sampleRestServiceClient.getShiftsForSchedule("anInvalidId"));
    }
}
