package io.beekeeper.connector.shiftsync.sample;

import io.beekeeper.connector.shiftsync.sample.client.SampleRestServiceClient;
import io.beekeeper.connector.shiftsync.sample.external.model.ExternalSchedule;
import io.beekeeper.integration.connector.api.config.GlobalConfiguration;
import io.beekeeper.integration.connector.shiftsync.api.domain.Schedule;
import io.beekeeper.integration.connector.shiftsync.api.importer.ScheduleProvider;
import io.beekeeper.integration.connector.shiftsync.api.importer.data.ScheduleImportConfiguration;
import io.beekeeper.integration.connector.shiftsync.api.importer.data.ScheduleImportResult;
import io.beekeeper.integration.connector.shiftsync.api.observer.ScheduleImportObserver;
import lombok.Builder;

import java.util.Collection;

@Builder
public class SampleScheduleImporter implements ScheduleProvider {
    private final GlobalConfiguration globalConfiguration;

    @Override
    public ScheduleImportResult fetchAvailableSchedules(
            ScheduleImportConfiguration scheduleImportConfiguration,
            ScheduleImportObserver scheduleImportObserver
    ) {
        final var connectorConfiguration = scheduleImportConfiguration.getConnectorConfigurationForTenant();
        final var sampleRestService = SampleRestServiceClient.create(
            connectorConfiguration.getProperty("baseUrl"),
            connectorConfiguration.getProperty("apiToken")
        );

        final Collection<ExternalSchedule> externalSchedules = sampleRestService.getSchedules();
        externalSchedules.stream()
            .map(this::toSchedule)
            .forEach(scheduleImportObserver::onNext);

        return new ScheduleImportResult();
    }

    private Schedule toSchedule(ExternalSchedule externalSchedule) {
        return Schedule.builder()
            .id(externalSchedule.getId())
            .name(externalSchedule.getLocationName())
            .build();
    }
}
