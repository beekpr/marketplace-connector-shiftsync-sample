package io.beekeeper.connector.shiftsync.sample;

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
import java.util.List;

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
        // final var sampleRestService = SampleRestServiceClient.create(
        // connectorConfiguration.getProperty("baseUrl"),
        // connectorConfiguration.getProperty("apiToken")
        // );

        final Collection<ExternalShift> externalShifts = List.of(
            new ExternalShift(
                    "11",
                    "2022-04-22T21:50:59Z",
                    "2022-04-22T21:59:59Z",
                    "1234",
                    "Title3",
                    "Desc",
                    "f4c54498-0976-446b-aca1-58350398e3a7"
            ),
            new ExternalShift(
                    "12",
                    "2022-04-22T21:50:59Z",
                    "2022-04-22T21:59:59Z",
                    "1234",
                    "Title4",
                    "Desc",
                    "f4c54498-0976-446b-aca1-58350398e3a7"
            ),
            new ExternalShift(
                    "13",
                    "2022-04-19T21:50:59Z",
                    "2022-04-19T21:59:59Z",
                    "789",
                    "Title9",
                    "Desc",
                    "f4c54498-0976-446b-aca1-58350398e3a7"
            ),
            new ExternalShift("14", "2022-04-23T21:50:59Z", "2022-04-23T21:59:59Z", "789", "Title10", "Desc", "1")
        );
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
