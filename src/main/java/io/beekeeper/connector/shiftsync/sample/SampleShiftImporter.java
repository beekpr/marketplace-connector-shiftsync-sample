package io.beekeeper.connector.shiftsync.sample;

import io.beekeeper.connector.shiftsync.sample.client.SampleRestServiceClient;
import io.beekeeper.connector.shiftsync.sample.external.model.ExternalShift;
import io.beekeeper.integration.connector.api.config.GlobalConfiguration;
import io.beekeeper.integration.connector.shiftsync.api.domain.Schedule;
import io.beekeeper.integration.connector.shiftsync.api.domain.Shift;
import io.beekeeper.integration.connector.shiftsync.api.domain.TimeWindow;
import io.beekeeper.integration.connector.shiftsync.api.importer.ShiftProvider;
import io.beekeeper.integration.connector.shiftsync.api.importer.data.ShiftImportConfiguration;
import io.beekeeper.integration.connector.shiftsync.api.importer.data.ShiftImportResult;
import io.beekeeper.integration.connector.shiftsync.api.observer.ShiftImportObserver;
import lombok.Builder;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Builder
public class SampleShiftImporter implements ShiftProvider {
    private final GlobalConfiguration globalConfiguration;

    @Override
    public ShiftImportResult fetchShifts(
            ShiftImportConfiguration shiftImportConfiguration,
            Schedule schedule,
            ShiftImportObserver shiftImportObserver
    ) {
        final var connectorConfiguration = shiftImportConfiguration.getConnectorConfigurationForTenant();
        final var sampleRestService = SampleRestServiceClient.create(
            connectorConfiguration.getProperty("baseUrl"),
            connectorConfiguration.getProperty("apiToken")
        );

        final Collection<ExternalShift> externalShifts = sampleRestService.getShiftsForSchedule(schedule.getId());
        externalShifts.stream()
            .map(this::toShift)
            .forEach(shiftImportObserver::onNext);

        return new ShiftImportResult();
    }

    private Shift toShift(ExternalShift externalShift) {
        return Shift.builder()
            .id(externalShift.getId())
            .description(externalShift.getDescription())
            .externalUserId(externalShift.getUserId())
            .title(externalShift.getTitle())
            .startAndEnd(
                TimeWindow.between(
                    convertDateStringToZonedDateTime(externalShift.getStart()),
                    convertDateStringToZonedDateTime(externalShift.getEnd())
                )
            )
            .build();
    }

    private ZonedDateTime convertDateStringToZonedDateTime(String date) {
        return ZonedDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    }
}
