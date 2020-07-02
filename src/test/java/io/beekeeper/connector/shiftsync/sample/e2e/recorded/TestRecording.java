package io.beekeeper.connector.shiftsync.sample.e2e.recorded;

import io.beekeeper.connector.shiftsync.sample.ExampleShiftRestService;
import io.beekeeper.connector.shiftsync.sample.SampleConnector;
import io.beekeeper.integration.connector.api.config.ConnectorConfigurationForTenant;
import io.beekeeper.integration.connector.api.config.GlobalConfiguration;
import io.beekeeper.integration.connector.shiftsync.api.domain.TimeWindow;
import io.beekeeper.integration.test.recording.shift.ShiftSyncRecorder;
import io.beekeeper.integration.test.recording.shift.ShiftSyncRecordingData;

import java.nio.file.Path;
import java.time.ZonedDateTime;

public class TestRecording {
    public static void main(String[] args) throws Exception {
        ExampleShiftRestService sampleService = new ExampleShiftRestService();
        sampleService.start();
        final var tenantConfiguration = configuration(sampleService.getBaseUrl(), sampleService.getApiToken());

        final ShiftSyncRecordingData shiftSyncRecordingData = ShiftSyncRecordingData.builder()
            .outputPath(Path.of("src/test/resources"))
            .globalConfiguration(new GlobalConfiguration())
            .tenantConfiguration(tenantConfiguration)
            .tenantPropertyToAnonymize("apiToken")
            .requestedShiftsTimeWindow(
                TimeWindow.between(ZonedDateTime.now().minusDays(7), ZonedDateTime.now().plusDays(7))
            )
            .build();

        final SampleConnector sampleConnector = new SampleConnector();
        final ShiftSyncRecorder shiftSyncRecorder = new ShiftSyncRecorder();

        shiftSyncRecorder.performImportAndRecordNetworkCalls(sampleConnector, "happyPath", shiftSyncRecordingData);
        sampleService.stop();
    }

    private static ConnectorConfigurationForTenant configuration(String baseUrl, String apiToken) {
        final ConnectorConfigurationForTenant tenantConfiguration = new ConnectorConfigurationForTenant();
        tenantConfiguration.setProperty("baseUrl", baseUrl);
        tenantConfiguration.setProperty("apiToken", apiToken);
        return tenantConfiguration;
    }
}
